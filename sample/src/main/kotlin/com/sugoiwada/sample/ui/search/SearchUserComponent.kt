package com.sugoiwada.sample.ui.search

import dagger.Subcomponent

@SearchUserScope
@Subcomponent
interface SearchUserComponent {
    fun inject(activity: SearchUserActivity)
}
