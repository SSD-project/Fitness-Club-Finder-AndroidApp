package com.wust.ssd.fitnessclubfinder.di

import com.wust.ssd.fitnessclubfinder.ui.camera.CameraViewModel
import com.wust.ssd.fitnessclubfinder.ui.searchClub.SearchClubViewModel
import dagger.Subcomponent

@Subcomponent
interface ViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelComponent
    }

    fun cameraViewModel(): CameraViewModel
    fun searchClubViewModel(): SearchClubViewModel
}