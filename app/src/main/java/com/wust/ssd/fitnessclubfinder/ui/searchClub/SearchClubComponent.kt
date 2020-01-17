package com.wust.ssd.fitnessclubfinder.ui.searchClub

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface SearchClubComponent: AndroidInjector<SearchClubFragment>{
    @Subcomponent.Factory
    interface Factory: AndroidInjector.Factory<SearchClubFragment>
}