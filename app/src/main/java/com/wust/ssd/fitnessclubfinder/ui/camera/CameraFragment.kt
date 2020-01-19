package com.wust.ssd.fitnessclubfinder.ui.camera

import android.graphics.SurfaceTexture
import android.graphics.drawable.GradientDrawable
import android.hardware.camera2.CameraDevice
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.CameraHelper
import com.wust.ssd.fitnessclubfinder.di.Injectable
import com.wust.ssd.fitnessclubfinder.ui.main.MainActivity
import com.wust.ssd.fitnessclubfinder.utils.WindowSizeUtil
import javax.inject.Inject
import kotlin.math.min

class CameraFragment : Fragment(), Injectable, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
    LocationSource {

    private val TAG = "CameraFragment"

    @Inject
    lateinit var camera: CameraHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var mapFragment: SupportMapFragment? = null
    var map: GoogleMap? = null

    private var viewModel: CameraViewModel? = null

    private var textureView: TextureView? = null
    private val stateCallback = CameraStateCallback()
    private val surfaceTextureListener = SurfaceTextureListener()
    private lateinit var clubsContainer: RelativeLayout
    private lateinit var mapView: LinearLayout

    private var locationListener: LocationSource.OnLocationChangedListener? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel == null)
            viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(CameraViewModel::class.java)

        viewModel?.clubsContainer = clubsContainer

        viewModel?.apply {
            requestUserLocationUpdates()

            nearbySearchRepository.startNearbyClubsApiCalls()

            markers.observe(this@CameraFragment, Observer { markers ->

                clubsContainer.removeAllViews()
                markers.forEach {
                    it.refresh()
                    clubsContainer.addView(it.view)
                }

            })
        }

        (activity as? MainActivity)?.userlocation?.observe(this, Observer {
            locationListener?.onLocationChanged(it)
            viewModel?.onLocationChanged(it)
            viewModel?.userLocationUpdate(it)


        })
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        setupMap()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_camera, container, false)
        .also {
            clubsContainer = it.findViewById(R.id.clubs_container)
            textureView = it.findViewById(R.id.texture_view)
            mapView = it.findViewById(R.id.map_container)
        }

    private fun setupMap() =
        activity?.let { _ ->
            val windowUtil = WindowSizeUtil(activity!!.windowManager)

            val height = windowUtil.getWindowHeight()
            val width = windowUtil.getWindowWidth()
            val radius = min(width / 2, height / 2)

            val shape = GradientDrawable()
            shape.cornerRadius = radius.toFloat()
            mapView.background = shape

            val params: LinearLayout.LayoutParams =
                mapView.layoutParams as LinearLayout.LayoutParams
            params.height = radius * 2 + 100
            params.topMargin = height - radius - 400

            mapView.clipToOutline = true
            mapView.layoutParams = params
            mapView.requestLayout()
        }

    override fun onResume() {
        super.onResume()
        camera.openBackgroundThread()
        viewModel?.compass?.onResume()
        viewModel?.markers?.observe(this, Observer { markers ->
            if (map !== null) viewModel!!.setupMap(map!!)
            viewModel!!.runWorker(markers, activity!!)
        })
        when {
            textureView?.isAvailable!! -> camera.onResume(stateCallback)
            else -> textureView!!.surfaceTextureListener = surfaceTextureListener
        }
    }


    override fun onStop() {
        super.onStop()
        camera.onStop()
        viewModel?.compass?.onPause()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.compass?.onPause()
    }


    inner class SurfaceTextureListener : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) = Unit
        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) = Unit
        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean = false

        override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
            camera.setupCamera()
            camera.openCamera(stateCallback)
            viewModel?.let {
                it.verticalViewAngle = camera.getVerticalViewAngle()
                it.horizontalViewAngle = camera.getHorizontalViewAngle()

                val displayMetrics = DisplayMetrics()
                activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                it.screenWidth = displayMetrics.widthPixels
                it.screenHeight = displayMetrics.heightPixels
            }
        }
    }

    inner class CameraStateCallback : CameraDevice.StateCallback() {
        override fun onOpened(p0: CameraDevice) {
            camera.createPreviewSession(p0, textureView?.surfaceTexture!!)
        }

        override fun onDisconnected(p0: CameraDevice) {
            camera.onDisconnected()
        }

        override fun onError(p0: CameraDevice, p1: Int) {
            camera.onDisconnected()
        }

    }

    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap?.let {
            map = googleMap
            viewModel?.setupMap(it)
            it.uiSettings.apply {
                isScrollGesturesEnabled = false
                isZoomGesturesEnabled = false
                isRotateGesturesEnabled = false
                isMyLocationButtonEnabled = true
            }
            it.isMyLocationEnabled = true
            it.isIndoorEnabled = true
            it.isBuildingsEnabled = true
            it.setLocationSource(this)

            viewModel?.location?.observe(this, Observer { loc ->
                val cameraUpdate =
                    CameraUpdateFactory.newLatLng(LatLng(loc.latitude, loc.longitude))
                it.animateCamera(cameraUpdate)
            })


        }

    }

    override fun onMapLoaded() = Unit
    override fun deactivate() {
        locationListener = null
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        p0?.let { locationListener = it }
        if (locationListener !== null && activity is MainActivity && (activity as MainActivity).userlocation.value?.latitude !== null) {
            locationListener!!.onLocationChanged((activity as MainActivity).userlocation.value)
        }


    }


}
