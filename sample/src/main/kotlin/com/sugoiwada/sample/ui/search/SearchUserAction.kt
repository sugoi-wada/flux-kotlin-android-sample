package com.sugoiwada.sample.ui.search

import com.sugoiwada.sample.data.repository.GitHubClient
import com.sugoiwada.sample.model.github.GitHubUser
import com.sugoiwada.sample.model.search.SearchModel
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

@SearchUserScope
class SearchUserAction @Inject constructor(private val gitHubClient: GitHubClient, private val dispatcher: SearchUserDispatcher) {
    var subscriptions: CompositeSubscription = CompositeSubscription()

    fun searchUser(query: String, page: Int) {
        subscriptions.clear()

        dispatcher.loading.onNext(true)

        if (query.isBlank()) {
            dispatcher.searchUser.onNext(0.to(SearchModel()))
            dispatcher.loading.onNext(false)
            return
        }

        gitHubClient.searchUsers(query, page)
                .subscribeOn(Schedulers.io())
                .doAfterTerminate {
                    dispatcher.loading.onNext(false)
                }
                .subscribe({ response ->
                    val ghUsers = response.items.map { GitHubUser(it.login, it.url, it.avatarUrl) }
                    val searchUser = SearchModel(query, ghUsers, response.totalCount, response.gitHubLinkHeader)
                    dispatcher.searchUser.onNext(page.to(searchUser))
                }, { e ->
                    dispatcher.error.onNext(e)
                })
                .let { subscriptions.add(it) }
    }
}
