package com.sugoiwada.sample.ui.search

import android.content.Context
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.florent37.viewanimator.ViewAnimator
import com.github.pwittchen.reactivenetwork.library.ReactiveNetwork
import com.jakewharton.rxbinding.support.v7.widget.scrollEvents
import com.jakewharton.rxbinding.view.clicks
import com.sugoiwada.sample.App
import com.sugoiwada.sample.R
import com.sugoiwada.sample.model.github.GitHubUser
import com.sugoiwada.sample.ui.view.queryText
import com.sugoiwada.sample.util.closeKeyboard
import com.sugoiwada.sample.util.isNearBottomEdge
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_search_user.*
import kotlinx.android.synthetic.main.item_progress.view.*
import kotlinx.android.synthetic.main.item_search_user.view.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchUserActivity : RxAppCompatActivity() {

    private val component by lazy { (application as App).appComponent.plus() }

    private val adapter by lazy { Adapter(this) }

    @Inject lateinit var searchUserAction: SearchUserAction

    @Inject lateinit var searchUserStore: SearchUserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
        component.inject(this)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        searchUserStore.searchUser
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { searchModel ->
                    itemCountText.text = searchModel.totalCountText
                    adapter.addUsers(searchModel.newElements)
                }

        searchUserStore.loading
                .distinctUntilChanged()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loading ->
                    adapter.isLoading = loading
                }

        searchUserStore.error
                .distinctUntilChanged()
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    Timber.e(e)
                    closeKeyboard()
                    AlertDialog.Builder(this)
                            .setMessage(getString(R.string.error_network))
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                }

        recycler.scrollEvents()
                .bindToLifecycle(this)
                .filter { recycler.isNearBottomEdge(5) }
                .filter { searchUserStore.isLoading.not() && searchUserStore.value?.nextPage != null }
                .map { searchUserStore.value?.searchkey.to(searchUserStore.value?.nextPage!!) }
                .distinctUntilChanged { lPair, rPair -> lPair.first == rPair.first && lPair.second == rPair.second }
                .map { it.second }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { nextPage ->
                    closeKeyboard()
                    searchUserAction.searchUser(searchUserStore.value?.searchkey!!, nextPage!!)
                }

        Observable.merge(
                ReactiveNetwork.observeNetworkConnectivity(this).map { it.state == NetworkInfo.State.CONNECTED },
                connectionFailure.clicks().map { true }
        )
                .bindToLifecycle(this)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isOnline ->
                    if (isOnline) hideNetworkProblemView()
                    else showNetworkProblemView()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchUserAction.subscriptions.clear()
        searchUserStore.subscriptions.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView
        searchView.queryText()
                .throttleLast(500, TimeUnit.MILLISECONDS)
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query ->
                    if (query.submitted) {
                        closeKeyboard()
                    }

                    if (searchUserStore.value?.searchkey == query.text) {
                        return@subscribe
                    }

                    adapter.deleteAllUsers()
                    searchUserAction.searchUser(query.text, 0)
                }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun showNetworkProblemView() {
        ViewAnimator.animate(connectionFailure)
                .translationY(-connectionFailure.measuredHeight.toFloat(), 0f)
                .duration(500)
                .start()

        connectionFailure.visibility = View.VISIBLE
    }

    private fun hideNetworkProblemView() {
        ViewAnimator.animate(connectionFailure)
                .translationY(0f, -connectionFailure.measuredHeight.toFloat())
                .duration(500)
                .start()
    }

    class Adapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private val USER = 0
            private val LOADING = 1
        }

        private val users: MutableList<GitHubUser> = mutableListOf()

        var isLoading: Boolean = false
            set(value) {
                if (field == value) {
                    return
                }

                field = value
                if (value) {
                    notifyItemInserted(users.size)
                } else {
                    notifyItemRemoved(users.size)
                }
            }

        override fun getItemCount(): Int = users.size + if (isLoading) 1 else 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                LOADING -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false))
                USER -> UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_user, parent, false))
                else -> throw IllegalAccessException("No viewTypes create.")
            }
        }

        override fun getItemViewType(position: Int): Int = if (users.size == position) LOADING else USER

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is UserViewHolder) {
                holder.name.text = users[position].name
                holder.url.text = users[position].url
                Glide.with(context).load(users[position].imageUrl).into(holder.avatar)
            }
        }

        fun addUsers(users: List<GitHubUser>) {
            if (isLoading) {
                notifyItemRemoved(this.users.size)
            }
            notifyItemRangeInserted(this.users.size, users.count())
            this.users.addAll(users)
        }

        fun deleteAllUsers() {
            notifyItemRangeRemoved(0, users.size)
            users.removeAll { true }
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.name
        val url: TextView = itemView.url
        val avatar: ImageView = itemView.avatar
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val loading: ProgressBar = itemView.progress
    }
}
