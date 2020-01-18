package com.wust.ssd.fitnessclubfinder.ui.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.PermissionHelper
import com.wust.ssd.fitnessclubfinder.di.Injectable
import com.wust.ssd.fitnessclubfinder.ui.login.LoginActivity
import java.util.jar.Manifest
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Injectable, LocationListener {
    private val TAG = this.javaClass.simpleName


    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    val userlocation = MutableLiveData<Location>()

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val permissionHelper = PermissionHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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


        setupPermissions()
        setupLocationListener()

    }

    private fun setupLocationListener() =
        if (ContextCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                this.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        } else null

    private fun setupPermissions() {
        val permissionsRequired = arrayOf(CAMERA, ACCESS_FINE_LOCATION)
        if (permissionHelper.hasPermissions(permissionsRequired))
            permissionHelper.requestPermissions(permissionsRequired)
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper.onRequestPermissionResult(permissions, grantResults)
    }

    override fun onLocationChanged(location: Location?) {
        location?.let { userlocation.postValue(it) }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) = Unit
    override fun onProviderEnabled(p0: String?) = Unit
    override fun onProviderDisabled(p0: String?) = Unit

}
