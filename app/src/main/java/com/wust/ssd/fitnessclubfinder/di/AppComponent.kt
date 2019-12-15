package com.wust.ssd.fitnessclubfinder.di

import com.wust.ssd.fitnessclubfinder.App
import com.wust.ssd.fitnessclubfinder.ui.login.LoginActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    LoginActivityModule::class

])
interface AppComponent: AndroidInjector<DaggerApplication> {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun build(): AppComponent
    }

    override fun inject(instance: DaggerApplication?)
}