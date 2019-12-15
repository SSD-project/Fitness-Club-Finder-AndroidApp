package com.wust.ssd.fitnessclubfinder.di

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.wust.ssd.fitnessclubfinder.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class NetworkModule {

    @Provides
    fun provideContext(app: App): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideAccount(app: App): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideGoogleSignInClient(app:App): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(app.applicationContext, gso)
    }
}