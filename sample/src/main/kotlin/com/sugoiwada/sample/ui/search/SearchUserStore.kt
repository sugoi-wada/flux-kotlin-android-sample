package com.sugoiwada.sample.ui.search

import com.sugoiwada.sample.model.github.GitHubUser
import com.sugoiwada.sample.model.search.SearchModel
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

@SearchUserScope
class SearchUserStore @Inject constructor(private val dispatcher: SearchUserDispatcher) {

    var value: SearchModel<GitHubUser>? = null
    var isLoading: Boolean = false

    val subscriptions = CompositeSubscription()

    val searchUser = SerializedSubject(BehaviorSubject.create<SearchModel<GitHubUser>>())
    val error = SerializedSubject(PublishSubject.create<Throwable>())
    val loading = SerializedSubject(PublishSubject.create<Boolean>())

    init {
        dispatcher.searchUser.asObservable()
                .subscribe { pair ->
                    val (page, searchUser) = pair
                    if (page == 0) {
                        value = searchUser
                        this.searchUser.onNext(searchUser)
                    } else {
                        value?.concat(searchUser)
                        this.searchUser.onNext(value)
                    }
                }
                .let { subscriptions.add(it) }

        dispatcher.error.subscribe { e -> error.onNext(e) }

        dispatcher.loading.subscribe {
            isLoading = it
            loading.onNext(it)
        }
    }
}