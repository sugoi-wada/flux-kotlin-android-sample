package com.sugoiwada.sample.ui.search

import com.sugoiwada.sample.model.github.GitHubUser
import com.sugoiwada.sample.model.search.SearchModel
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject
import javax.inject.Inject

@SearchUserScope
class SearchUserDispatcher @Inject constructor() {
    val searchUser = SerializedSubject(PublishSubject.create<Pair<Int, SearchModel<GitHubUser>>>())
    val error = SerializedSubject(PublishSubject.create<Throwable>())
    val loading = SerializedSubject(PublishSubject.create<Boolean>())
}
