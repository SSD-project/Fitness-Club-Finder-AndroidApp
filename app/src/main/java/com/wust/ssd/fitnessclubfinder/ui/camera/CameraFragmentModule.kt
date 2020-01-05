package com.wust.ssd.fitnessclubfinder.ui.camera

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class CameraFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(CameraFragment::class)
    internal abstract fun bindCameraFragmentFactory(factory: CameraFragmentComponent.Factory): AndroidInjector.Factory<*>
}