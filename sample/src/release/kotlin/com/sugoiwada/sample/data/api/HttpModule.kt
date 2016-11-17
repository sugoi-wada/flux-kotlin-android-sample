package com.sugoiwada.sample.data.api

import com.sugoiwada.sample.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class HttpModule {
    @Provides
    @AppScope
    fun provideOkHttpClient(gitHubInterceptor: GitHubInterceptor): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(gitHubInterceptor)
            .build()
}
