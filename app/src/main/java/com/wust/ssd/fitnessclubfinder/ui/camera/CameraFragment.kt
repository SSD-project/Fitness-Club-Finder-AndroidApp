package com.wust.ssd.fitnessclubfinder.ui.camera

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraDevice
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.ARMarker
import com.wust.ssd.fitnessclubfinder.common.CameraHelper
import com.wust.ssd.fitnessclubfinder.di.Injectable
import javax.inject.Inject

class CameraFragment : Fragment(), Injectable, LocationListener {

    private val TAG = "CameraFragment"

    @Inject
    lateinit var camera: CameraHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: CameraViewModel? = null

    private var textureView: TextureView? = null
    private val stateCallback = CameraStateCallback()
    private val surfaceTextureListener = SurfaceTextureListener()
    private lateinit var clubsContainer: RelativeLayout


    @SuppressLint("MissingPermission")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel == null)
            viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(CameraViewModel::class.java)

        viewModel!!.requestUserLocationUpdates()

        viewModel?.location?.observe(this, Observer {
            Log.e("CameraLocation", it.latitude.toString())
        })

        viewModel?.nearbySearchRepository?.startNearbyClubsApiCalls()

        viewModel?.markers?.observe(this, Observer { markers ->
            clubsContainer.removeAllViews()

            markers.forEachIndexed { index, it ->
                it.verticalPosition = 100F * index
                it.refresh()
                clubsContainer.addView(it.view)

            }

        })

        val locationManager =
            activity?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
//        val textView: TextView = root.findViewById(R.id.text_camera)
//        viewModel?.text?.observe(this, Observer {
//            textView.text = it
//        })
//        imageView = root.findViewById(R.id.imageView)
        clubsContainer = root.findViewById(R.id.clubs_container)

        textureView = root.findViewById(R.id.texture_view)


        return root
    }


    override fun onResume() {
        super.onResume()
        camera.openBackgroundThread()
        when {
            textureView?.isAvailable!! -> camera.onResume(stateCallback)
            else -> textureView!!.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onLocationChanged(userLocation: Location?) {
//        Toast.makeText(context, "$TAG onLocationChanged", Toast.LENGTH_SHORT).show()
        userLocation?.let {
            viewModel?.onLocationChanged(it)
        }

    }

    override fun onStop() {
        super.onStop()
        camera.onStop()
    }


    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}


    inner class SurfaceTextureListener : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) = Unit
        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) = Unit
        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean = false

        override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
            camera.setupCamera()
            camera.openCamera(stateCallback)
        }
    }

    inner class CameraStateCallback : CameraDevice.StateCallback() {
        override fun onOpened(p0: CameraDevice) {
            camera.createPreviewSession(p0, textureView?.surfaceTexture!!)
            activity?.runOnUiThread {
                //                clubs.forEach {
//                    it.refresh()
//                }
//                imageView.setImageBitmap(
//                    viewModel?.createBitmapWithClubs(
//                        resources.displayMetrics,
//                        camera.previewSize!!
//                    )
//                )
            }
        }

        override fun onDisconnected(p0: CameraDevice) {
            camera.onDisconnected()
        }

        override fun onError(p0: CameraDevice, p1: Int) {
            camera.onDisconnected()
        }

    }
}