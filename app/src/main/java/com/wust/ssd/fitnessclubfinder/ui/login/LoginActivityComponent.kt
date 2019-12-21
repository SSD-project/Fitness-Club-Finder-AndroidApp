package com.wust.ssd.fitnessclubfinder.ui.login

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface LoginActivityComponent: AndroidInjector<LoginActivity>{
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<LoginActivity>
}