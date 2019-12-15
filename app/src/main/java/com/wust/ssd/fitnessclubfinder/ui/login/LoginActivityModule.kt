package com.wust.ssd.fitnessclubfinder

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module//(subcomponents = [LoginActivityComponent::class])
abstract class LoginActivityModule {
    @Binds
    @IntoMap
    @ClassKey(LoginActivity::class)
    internal abstract fun bindMainActivityFactory(factory: LoginActivityComponent.Factory): AndroidInjector.Factory<*>
}