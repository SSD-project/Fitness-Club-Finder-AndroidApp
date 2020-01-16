package com.wust.ssd.fitnessclubfinder.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.ArCoreApk
import com.google.ar.core.exceptions.*
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.CameraPermissionHelper
import com.wust.ssd.fitnessclubfinder.di.Injectable
import com.wust.ssd.fitnessclubfinder.ui.login.LoginActivity
import java.lang.Exception
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable {
    private val TAG = this.javaClass.simpleName


    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient


    private lateinit var appBarConfiguration: AppBarConfiguration

    private var installRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_camera,
                R.id.nav_search_club,
                R.id.nav_favourites,
                R.id.nav_my_account
            ), drawerLayout
        )

        val b = findViewById<Button>(R.id.logout_button)
        b.setOnClickListener {
            if (it.id == R.id.logout_button)
                signOut()
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.augmented_reality, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOut() =
        mGoogleSignInClient.signOut()?.addOnCompleteListener {
            Toast
                .makeText(this, "Signed out successfully", Toast.LENGTH_SHORT)
                .show()

            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }

    override fun onResume() {
        super.onResume()
        try {
            if (ArCoreApk.getInstance().requestInstall(
                    this,
                    !installRequested
                ) === ArCoreApk.InstallStatus.INSTALL_REQUESTED
            ) {
                installRequested = true
                return
            }
        } catch (e: Exception) {
            val message = when(e){
                is UnavailableArcoreNotInstalledException -> "Please install ARCore"
                is UnavailableUserDeclinedInstallationException -> "Please install ARCore"
                is UnavailableApkTooOldException -> "Please update ARCore"
                is UnavailableSdkTooOldException -> "Please update this app"
                is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
                else -> "Failed to create AR session"
            }
            Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            Log.e(TAG, message)

        }
        //TODO: refactor CameraPermissionHelper, use DI...
        if (!CameraPermissionHelper().hasCameraPermission(this)) {
            CameraPermissionHelper().requestCameraPermission(this)
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(
            this,
            "Camera permission is needed to run this application",
            Toast.LENGTH_LONG
        )
            .show()
        if (!CameraPermissionHelper().shouldShowRequestPermissionRationale(this)) {
            CameraPermissionHelper().launchPermissionSettings(this)
        }
        finish()

    }

}
