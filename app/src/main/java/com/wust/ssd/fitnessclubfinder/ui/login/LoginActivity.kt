package com.wust.ssd.fitnessclubfinder.ui.login

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
import com.wust.ssd.fitnessclubfinder.di.Injectable
import com.wust.ssd.fitnessclubfinder.R
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), Injectable {


    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            when (it.id) {
                R.id.sign_in_button -> signIn()
            }
        }
    }

    private fun signIn() =
        startActivityForResult(mGoogleSignInClient.signInIntent, RC_SIGN_IN)


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        completedTask.getResult(ApiException::class.java)
        finish()

    } catch (e: ApiException) {
        Log.w("ERROR", "signInResult: failed code=" + e.statusCode)
    }
}
