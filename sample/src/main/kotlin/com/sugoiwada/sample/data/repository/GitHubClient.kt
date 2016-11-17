package com.sugoiwada.sample.data.repository

import com.sugoiwada.sample.AppScope
import com.sugoiwada.sample.data.api.GitHubService
import com.sugoiwada.sample.model.github.GitHubLinkHeader
import javax.inject.Inject

@AppScope
class GitHubClient @Inject constructor(private val service: GitHubService) {
    fun searchUsers(query: String, page: Int) =
            service.listUsers(query, page)
                    .map { response ->
                        response.body().apply {
                            response.headers().get("Link")?.let { linkHeaderString ->
                                gitHubLinkHeader = GitHubLinkHeader.parse(linkHeaderString)
                            }
                        }
                    }
}
