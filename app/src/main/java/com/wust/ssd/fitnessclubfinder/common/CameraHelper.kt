package com.wust.ssd.fitnessclubfinder.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraCharacteristics.*
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import androidx.core.app.ActivityCompat
import java.util.*
import kotlin.math.PI
import kotlin.math.atan


class CameraHelper(private val context: Context) {

    private val cameraManager: CameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    var previewSize: Size? = null
    private var cameraId: String? = null
    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var captureRequest: CaptureRequest? = null

    fun setupCamera() {
        try {
            cameraManager.cameraIdList.forEach {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
                if (cameraCharacteristics.get(LENS_FACING) == CameraMetadata.LENS_FACING_BACK) {
                    cameraCharacteristics[SENSOR_INFO_PHYSICAL_SIZE]
                    val streamConfigurationMap =
                        cameraCharacteristics.get(SCALER_STREAM_CONFIGURATION_MAP)
                    previewSize =
                        streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)!![0]
                    this.cameraId = it
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun openCamera(stateCallback: CameraDevice.StateCallback) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                cameraId?.let { id ->
                    cameraManager.openCamera(id, stateCallback, backgroundHandler)
                    cameraCharacteristics = cameraManager.getCameraCharacteristics(id)
                }

            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    fun getHorizontalViewAngle(): Float? {
        val lensFocalLength = cameraCharacteristics?.get(LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.get(0)
        val sensorPhysicalSize = cameraCharacteristics?.get(SENSOR_INFO_PHYSICAL_SIZE)?.width

        if (lensFocalLength !== null && sensorPhysicalSize !== null) {
            return (180 / PI).toFloat() * (2F * atan(sensorPhysicalSize / (2F * lensFocalLength)))
        }
        return null
    }

    fun getVerticalViewAngle(): Float? {
        val lensFocalLength = cameraCharacteristics?.get(LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.get(0)
        val sensorPhysicalSize = cameraCharacteristics?.get(SENSOR_INFO_PHYSICAL_SIZE)?.height
        if (lensFocalLength !== null && sensorPhysicalSize !== null) {
            return (180 / PI).toFloat() * 2F * atan(sensorPhysicalSize / (2F * lensFocalLength))
        }
        return null
    }

    fun openBackgroundThread() {
        backgroundThread = HandlerThread("cameraBackgroundThread")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    private fun closeBackgroundThread() {
        if (backgroundHandler !== null) {
            backgroundThread?.quitSafely()
            backgroundThread = null
            backgroundHandler = null
        }
    }

    private fun closeCamera() {
        if (cameraCaptureSession !== null) {
            cameraCaptureSession!!.close()
            cameraCaptureSession = null
        }
        if (cameraDevice !== null) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }


    fun createPreviewSession(camera: CameraDevice, surfaceTexture: SurfaceTexture) {
        cameraDevice = camera

        try {
            previewSize?.let {
                surfaceTexture.setDefaultBufferSize(it.width, it.height)
            }
            val previewSurface = Surface(surfaceTexture)

            cameraDevice?.createCaptureSession(
                Collections.singletonList(previewSurface),
                CameraCaptureSessionCallback(previewSurface),
                backgroundHandler
            )

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun onStop() {
        closeCamera()
        closeBackgroundThread()
    }

    fun onResume(stateCallback: CameraDevice.StateCallback) {
        setupCamera()
        openCamera(stateCallback)
    }

    fun onDisconnected() {
        cameraDevice?.close()
        cameraDevice = null
    }

    inner class CameraCaptureSessionCallback(private val previewSurface: Surface) :
        CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(p0: CameraCaptureSession) = Unit

        override fun onConfigured(p0: CameraCaptureSession) {
            if (cameraDevice == null) return

            try {
                captureRequest =
                    cameraDevice!!
                        .createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                        .apply { addTarget(previewSurface) }
                        .build()
                cameraCaptureSession = p0
                cameraCaptureSession!!.setRepeatingRequest(
                    captureRequest!!,
                    null,
                    backgroundHandler
                )

            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

    }
}