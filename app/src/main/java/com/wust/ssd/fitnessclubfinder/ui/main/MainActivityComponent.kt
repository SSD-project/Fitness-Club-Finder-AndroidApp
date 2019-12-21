package com.wust.ssd.fitnessclubfinder.ui.main

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MainActivityComponent:AndroidInjector<MainActivity>{
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<MainActivity>
}