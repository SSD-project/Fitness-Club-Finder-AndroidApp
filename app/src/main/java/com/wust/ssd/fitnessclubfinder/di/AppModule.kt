package com.wust.ssd.fitnessclubfinder.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.wust.ssd.fitnessclubfinder.App
import com.wust.ssd.fitnessclubfinder.common.CameraHelper
import com.wust.ssd.fitnessclubfinder.common.DisplayRotationHelper
import com.wust.ssd.fitnessclubfinder.common.network.RetrofitFactory
import com.wust.ssd.fitnessclubfinder.common.repository.NearbySearchRepository
import com.wust.ssd.fitnessclubfinder.common.repository.RestApiRepository
import com.wust.ssd.fitnessclubfinder.ui.camera.CameraFragmentComponent
import com.wust.ssd.fitnessclubfinder.ui.login.LoginActivityComponent
import com.wust.ssd.fitnessclubfinder.ui.main.MainActivityComponent
import com.wust.ssd.fitnessclubfinder.utils.RxSchedulersFacade
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(
    subcomponents = [
        ViewModelComponent::class,
        LoginActivityComponent::class,
        MainActivityComponent::class,
        CameraFragmentComponent::class
    ]
)
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext


    @Singleton
    @Provides
    fun provideViewModelFactory(viewModelBuilder: ViewModelComponent.Builder): ViewModelProvider.Factory =
        ViewModelFactory(viewModelBuilder.build())

    @Provides
    @Singleton
    fun provideAccount(app: App): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(app.applicationContext)

    @Provides
    fun provideGoogleSignInClient(app: App): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(app.applicationContext, gso)
    }


    @Provides
    fun provideDisplayRotationHelper(app: App) = DisplayRotationHelper(app.applicationContext)

    @Singleton
    @Provides
    fun provideRestApiRepository(): RestApiRepository = RestApiRepository(RetrofitFactory())

    @Provides
    fun provideRxSchedulersFacade(): RxSchedulersFacade = RxSchedulersFacade()

    @Singleton
    @Provides
    fun provideNearbySearchRepository(
        app: Application,
        restApiRepository: RestApiRepository,
        rxSchedulersFacade: RxSchedulersFacade
    ): NearbySearchRepository =
        NearbySearchRepository(restApiRepository, app.applicationContext, rxSchedulersFacade)

    @Provides
    fun provideCameraHelper(app: App) = CameraHelper(app.applicationContext)
}