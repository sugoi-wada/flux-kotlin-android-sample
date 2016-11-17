package com.sugoiwada.sample.data.api

import com.sugoiwada.sample.AppScope
import com.sugoiwada.sample.data.api.mock.MockGitHubService
import dagger.Module
import dagger.Provides

@Module
class ApiModule {
    @Provides
    @AppScope
    fun provideGitHubService(): GitHubService = MockGitHubService()
}
