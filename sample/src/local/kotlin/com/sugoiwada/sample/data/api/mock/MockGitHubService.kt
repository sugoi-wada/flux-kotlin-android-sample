package com.sugoiwada.sample.data.api.mock

import com.sugoiwada.sample.AppScope
import com.sugoiwada.sample.data.api.GitHubService
import com.sugoiwada.sample.data.api.dto.GitHubResponse
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

@AppScope
class MockGitHubService @Inject constructor() : GitHubService {
    override fun listUsers(query: String, page: Int, perPage: Int): Observable<Response<GitHubResponse>> =
            Observable.just(MockGitHub.MOCK_GIT_HUB_RESPONSE)
}
