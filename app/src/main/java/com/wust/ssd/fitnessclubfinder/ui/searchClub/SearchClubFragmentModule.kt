package com.wust.ssd.fitnessclubfinder.ui.searchClub

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class SearchClubFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SearchClubFragment::class)
    internal abstract fun bindSearchClubFragmentFactory(factory: SearchClubComponent.Factory): AndroidInjector.Factory<*>
}