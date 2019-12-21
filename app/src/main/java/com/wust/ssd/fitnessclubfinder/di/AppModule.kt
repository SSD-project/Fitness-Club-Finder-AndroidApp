package com.wust.ssd.fitnessclubfinder.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.wust.ssd.fitnessclubfinder.App
import com.wust.ssd.fitnessclubfinder.ui.login.LoginActivityComponent
import com.wust.ssd.fitnessclubfinder.ui.main.MainActivity
import com.wust.ssd.fitnessclubfinder.ui.main.MainActivityComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(subcomponents = [LoginActivityComponent::class, MainActivityComponent::class])
class AppModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideAccount(app: App): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(app.applicationContext)

    @Provides
    fun provideGoogleSignInClient(app:App): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(app.applicationContext, gso)
    }
}