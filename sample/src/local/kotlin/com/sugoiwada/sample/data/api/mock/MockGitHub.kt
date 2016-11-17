package com.sugoiwada.sample.data.api.mock

import com.sugoiwada.sample.data.api.dto.GitHubResponse
import com.sugoiwada.sample.data.api.dto.Item
import okhttp3.Headers
import retrofit2.Response

object MockGitHub {
    val MOCK_GIT_HUB_RESPONSE: Response<GitHubResponse> =
            Response.success(GitHubResponse(10, createItems(10)), Headers.of())

    private fun createItems(count: Int): List<Item> =
            (0..count - 1).map {
                val url = "https://placeholdit.imgix.net/~text?txtsize=33&txt=$it&w=150&h=150"
                Item("name$it", url, url)
            }
}
