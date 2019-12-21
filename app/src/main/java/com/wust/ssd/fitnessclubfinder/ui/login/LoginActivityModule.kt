package com.wust.ssd.fitnessclubfinder.ui.login

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class LoginActivityModule {
    @Binds
    @IntoMap
    @ClassKey(LoginActivity::class)
    internal abstract fun bindLoginActivityFactory(factory: LoginActivityComponent.Factory): AndroidInjector.Factory<*>
}