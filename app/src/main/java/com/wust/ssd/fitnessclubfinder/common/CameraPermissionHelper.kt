package com.wust.ssd.fitnessclubfinder.common

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CameraPermissionHelper {
    companion object {
        private const val CAMERA_PERMISSION_CODE = 0
    }

    fun hasCameraPermission(activity: Activity): Boolean = ContextCompat.checkSelfPermission(
        activity,
        CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    fun requestCameraPermission(activity: Activity) = ActivityCompat.requestPermissions(
        activity,
        arrayOf(CAMERA),
        CAMERA_PERMISSION_CODE
    )

    fun shouldShowRequestPermissionRationale(activity: Activity) =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA)

    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package",activity.packageName, null)
        activity.startActivity(intent)

    }
}