package com.sugoiwada.sample.data.api

import com.sugoiwada.sample.AppScope
import com.sugoiwada.sample.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@AppScope
class GitHubInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
            chain.proceed(chain.request().newBuilder()
                    .addHeader("Authorization", "token ${BuildConfig.GITHUB_TOKEN}")
                    .build())

}
