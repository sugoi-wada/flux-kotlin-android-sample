package com.sugoiwada.sample.data.api

import com.github.aurae.retrofit2.LoganSquareConverterFactory
import com.sugoiwada.sample.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

@Module
class ApiModule {
    @Provides
    @AppScope
    fun provideGitHubService(okHttpClient: OkHttpClient): GitHubService = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(okHttpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(LoganSquareConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
}
