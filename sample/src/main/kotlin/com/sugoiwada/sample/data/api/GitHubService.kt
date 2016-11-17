package com.sugoiwada.sample.data.api

import com.sugoiwada.sample.data.api.dto.GitHubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface GitHubService {

    @GET("/search/users")
    fun listUsers(@Query("q") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int = 30): Observable<Response<GitHubResponse>>
}
