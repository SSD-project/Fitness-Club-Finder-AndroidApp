package com.wust.ssd.fitnessclubfinder.ui.camera

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.MainRenderer
import com.wust.ssd.fitnessclubfinder.common.model.UserLocation
import com.wust.ssd.fitnessclubfinder.di.Injectable
import javax.inject.Inject

class CameraFragment : Fragment(), Injectable, LocationListener {

    private val TAG = "CameraFragment"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var renderer:MainRenderer

    private var viewModel: CameraViewModel? = null

    private lateinit var mSurfaceView: GLSurfaceView

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
        viewModel?.nearbyClubs?.observe(this, Observer {
            Log.e(TAG,it.toString())
        })
        viewModel?.nearbySearchRepository?.startNearbyClubsApiCalls()

        val locationManager = activity?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val textView: TextView = root.findViewById(R.id.text_camera)
        viewModel?.text?.observe(this, Observer {
            textView.text = it
        })

        mSurfaceView = root.findViewById(R.id.surface_view)

        mSurfaceView.apply {
            preserveEGLContextOnPause = true
            preserveEGLContextOnPause = true
            setEGLContextClientVersion(2)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

        return root
    }


    override fun onPause() {
        super.onPause()
        mSurfaceView.onPause()
        renderer.onPause()

    }


    override fun onResume() {
        super.onResume()
        renderer.onResume()
        mSurfaceView.onResume()
    }

    override fun onLocationChanged(userLocation: Location?) {
        Toast.makeText(context, "$TAG onLocationChanged", Toast.LENGTH_SHORT).show()
        userLocation?.let {
            viewModel?.onLocationChanged(it)
        }

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}
}