package com.wust.ssd.fitnessclubfinder.common

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {
    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 302
    }

    fun hasPermissions(permissions: Array<out String>): Boolean =
        permissions.all {
            return ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }



fun requestPermissions(permissions: Array<String>) =
    ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE)

private fun launchPermissionSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.fromParts("package", activity.packageName, null)
    activity.startActivity(intent)
}

fun onRequestPermissionResult(
    permissions: Array<out String>,
    grantResults: IntArray
) {

    grantResults.mapIndexed { index, result ->

        if (result == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(
                activity,
                "${permissions[index]} permission is needed to run this application",
                Toast.LENGTH_LONG
            )
                .show()
            launchPermissionSettings()
            activity.finish()
        }
    }

}
}