package com.wust.ssd.fitnessclubfinder.ui.camera

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface CameraFragmentComponent: AndroidInjector<CameraFragment> {
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<CameraFragment>
}