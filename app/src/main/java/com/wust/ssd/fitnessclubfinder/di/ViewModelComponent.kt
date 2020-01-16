package com.wust.ssd.fitnessclubfinder.di

import com.wust.ssd.fitnessclubfinder.ui.camera.CameraViewModel
import dagger.Subcomponent

@Subcomponent
interface ViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelComponent
    }

    fun cameraViewModel(): CameraViewModel
}