package com.wust.ssd.fitnessclubfinder.ui.main

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)
    internal abstract fun bindMainActivityFactory(factory: MainActivityComponent.Factory): AndroidInjector.Factory<*>
}