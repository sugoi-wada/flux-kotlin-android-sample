package com.sugoiwada.sample

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber
import javax.inject.Inject

@AppScope
class AppLifecycleImpl @Inject constructor(var app: Application) {
    fun onCreate() {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(app)
    }
}