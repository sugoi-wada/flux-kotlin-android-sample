package com.sugoiwada.sample

import com.sugoiwada.sample.data.api.ApiModule
import com.sugoiwada.sample.data.api.HttpModule
import com.sugoiwada.sample.ui.search.SearchUserComponent
import dagger.Component

@AppScope
@Component(modules = arrayOf(HttpModule::class, AppModule::class, ApiModule::class))
interface AppComponent {

    fun inject(app: App)

    fun plus(): SearchUserComponent
}
