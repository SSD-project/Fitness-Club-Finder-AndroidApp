package com.wust.ssd.fitnessclubfinder

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent//(modules = [AppModule::class])
interface LoginActivityComponent: AndroidInjector<LoginActivity>{
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<LoginActivity>
}