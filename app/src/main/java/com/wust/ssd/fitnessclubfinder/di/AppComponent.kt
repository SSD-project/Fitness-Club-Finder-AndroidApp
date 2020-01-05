package com.wust.ssd.fitnessclubfinder.di

import com.wust.ssd.fitnessclubfinder.App
import com.wust.ssd.fitnessclubfinder.ui.camera.CameraFragmentModule
import com.wust.ssd.fitnessclubfinder.ui.login.LoginActivityModule
import com.wust.ssd.fitnessclubfinder.ui.main.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        LoginActivityModule::class,
        MainActivityModule::class,
        CameraFragmentModule::class
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: DaggerApplication?)
}