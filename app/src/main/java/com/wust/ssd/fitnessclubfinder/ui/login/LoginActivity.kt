package com.wust.ssd.fitnessclubfinder

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), Injectable {


    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            Log.e("LoginActivity", mGoogleSignInClient.instanceId.toString())
            when (it.id) {
                R.id.sign_in_button -> signIn()
            }
        }
    }

    fun signIn() =
        startActivityForResult(mGoogleSignInClient?.signInIntent, RC_SIGN_IN)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {

        val intent = Intent(this, AugmentedRealityActivity::class.java)
        startActivity(intent)

    } catch (e: ApiException) {
        Log.w("ERROR", "signInResult: failed code=" + e.statusCode)
    }
}
