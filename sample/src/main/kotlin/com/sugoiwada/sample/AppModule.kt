package com.sugoiwada.sample

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(val app: Application) {

    @Provides
    fun provideApplication(): Application = app

    @Provides
    fun provideContext(): Context = app

}
