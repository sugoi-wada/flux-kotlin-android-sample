package com.sugoiwada.sample

import android.app.Application
import javax.inject.Inject

@AppScope
class AppLifecycleImpl @Inject constructor(var app: Application) {
    fun onCreate() {
    }
}