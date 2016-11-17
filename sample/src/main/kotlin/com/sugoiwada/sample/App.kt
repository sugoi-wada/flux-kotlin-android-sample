package com.sugoiwada.sample

import android.app.Application
import okhttp3.OkHttpClient
import javax.inject.Inject

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    @Inject lateinit var appLifecycleImpl: AppLifecycleImpl
    @Inject lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        appLifecycleImpl.onCreate()

        AppGlideModule.registerComponents(this, okHttpClient)
    }
}
