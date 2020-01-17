package com.wust.ssd.fitnessclubfinder.ui.camera

import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import android.widget.RelativeLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.ARMarker
import com.wust.ssd.fitnessclubfinder.common.MarkerPositionHelper
import com.wust.ssd.fitnessclubfinder.common.model.Club
import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import com.wust.ssd.fitnessclubfinder.common.repository.NearbyClubsObserver
import com.wust.ssd.fitnessclubfinder.common.repository.NearbySearchRepository
import com.wust.ssd.fitnessclubfinder.utils.DrawableUtil
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.concurrent.thread
import kotlin.math.abs

class CameraViewModel @Inject constructor(
    private val context: Context,
    val nearbySearchRepository: NearbySearchRepository,
    private val drawableUtil: DrawableUtil
) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is camera Fragment"
    }
    private var disposable = CompositeDisposable()
    val location = MutableLiveData<Location>()
    val nearbyClubs = MutableLiveData<List<Club>>()
    val markers = MutableLiveData<List<ARMarker>>()
    val text: LiveData<String> = _text

    var screenWidth: Int? = null
    var screenHeight: Int? = null
    lateinit var clubsContainer: RelativeLayout

    var horizontalViewAngle: Float? = null
    var verticalViewAngle: Float? = null
    val compass = CompassSensor(context)


    val clubs = mutableListOf<ARMarker>()


    fun requestUserLocationUpdates() {
        disposable.add(nearbySearchRepository.subscribe(DataObserver()))
    }


    private fun userLocationUpdate(data: Location) = location.postValue(data)

    private fun nearbyClubsUpdate(data: List<Club>) = nearbyClubs.postValue(data)

    private fun markersUpdate(data: List<ARMarker>) = markers.postValue(data)

    private fun countBearingsAndDistances(markers: List<ARMarker>, location: Location) {
        markers.forEach {
            val loc = Location(it.club.name)
            loc.latitude = it.club.geometry.location.lat
            loc.longitude = it.club.geometry.location.lng
            it.bearing = location.bearingTo(loc)
            it.distance = location.distanceTo(loc)
        }
    }


    private fun getVerticalPosition(
        distance: Float,
        maxDist: Double,
        minDist: Double
    ) = ((distance - minDist) / (maxDist - minDist)).toFloat()

    fun runWorker(
        markers: List<ARMarker>
        , activity: Activity
    ) {


        val markerPosition = MarkerPositionHelper(horizontalViewAngle!!, verticalViewAngle!!)
        thread(start = true) {
            while (
                true
            ) {
                if (location.value !== null) {
                    countBearingsAndDistances(markers, location.value!!)
                    val parallax = markerPosition.getVerticalParallax(compass.pitch!!)
                    val maxY = 1850f//TODO get height from parent container in the air
                    val minY = 20f
                    val minVerticalPosition =
                        markerPosition.getMinVerticalPosition(parallax, minY, maxY)

                    val maxVerticalPosition =
                        markerPosition.getMaxVerticalPosition(parallax, minY, maxY)


                    val maxDist = markerPosition.getBoundaryDistances(true, markers)
                    val minDist = markerPosition.getBoundaryDistances(false, markers)

                    markers.map {
                        it.marginLeft =
                            markerPosition.getHorizontalPosition(
                                it.bearing!!,
                                compass.azimuth
                            ).let { marginLeft ->
                                when {
                                    marginLeft < 0 -> {
                                        it.icon = ARMarker.ARROW_LEFT
                                        0.01F//set to the left margin
                                    }
                                    marginLeft > 1 -> {
                                        it.icon = ARMarker.ARROW_RIGHT
                                        0.9F//set to the right margin
                                    }
                                    else -> {
                                        it.icon = ARMarker.MARKER_DEFAULT
                                        marginLeft
                                    }
                                }
                            } * screenWidth!!


                        val relativeVerticalPosition =
                            getVerticalPosition(it.distance!!, maxDist, minDist)


                        it.marginTop =
                            markerPosition.getMarginTop(
                                relativeVerticalPosition,
                                maxVerticalPosition,
                                minVerticalPosition
                            )


                        val layoutParams = it.refresh()
                        val newIcon = updateMakerIcon(it)
                        activity.runOnUiThread {
                            newIcon?.let { icon -> it.view.background = icon }
                            it.view.layoutParams = layoutParams
                            it.view.requestLayout()
                        }
                    }


                    Thread.sleep(33)
                }
            }
        }
    }

    private fun updateMakerIcon(marker: ARMarker): BitmapDrawable? {
        if (marker.icon !== marker.currentIcon) {

            marker.currentIcon = marker.icon
            return drawableUtil.importResource(
                marker.icon,
                R.drawable::class.java,
                64,
                64
            )
        }
        return null
    }


    fun onLocationChanged(userLocation: Location) {
        userLocationUpdate(userLocation)

        nearbySearchRepository.apply {
            if (lastLocation === null ||
                abs(userLocation.latitude - lastLocation!!.latitude) >= 0.00002 ||
                abs(userLocation.longitude - lastLocation!!.longitude) >= 0.00002 ||
                userLocation.speed >= 0.5
            ) {
                lastLocation = userLocation
                positionChangeFlag = true

            }
            this.next()
        }
    }

    inner class DataObserver : NearbyClubsObserver() {

        override fun onError(e: Throwable) {
            Log.e("nearby clubs error", e.stackTrace.toString())
        }


        override fun onComplete() {}
        override fun onNext(t: NearbySearchAPIResult) {
            if (t.results.isNotEmpty()) {
                nearbyClubsUpdate(t.results)

                val arMarkers = t.results.map { club ->
                    ARMarker(context, club)
                }

                if (markers.value === null || isEqualsToMarkers(t))
                    markersUpdate(arMarkers)
            }
        }

        private fun isEqualsToMarkers(t: NearbySearchAPIResult) =
            t.results.map { it.id } === markers.value!!.map { it.club.id }

    }


    inner class CompassSensor(context: Context) : SensorEventListener {

        var azimuth: Float = 0F
        var pitch: Float = 0F
        var roll: Float = 0F

        private var sensorManager: SensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        private var accelerometer: Sensor

        init {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
            sensorManager.registerListener(
                this, accelerometer, SensorManager.SENSOR_DELAY_GAME
            )
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let { e ->
                azimuth = exponentialSmoothing360(azimuth, e.values[0])
                pitch = exponentialSmoothing180(pitch, e.values[1])
                roll = exponentialSmoothing180(roll, e.values[2])

            }

        }

        fun onResume() = sensorManager.registerListener(
            this, accelerometer, SensorManager.SENSOR_DELAY_GAME
        )

        fun onPause() = sensorManager.unregisterListener(this)

        private fun exponentialSmoothing360(old: Float, measure: Float): Float = when {
            old - measure > 180 -> measure + 360
            measure - old > 180 -> measure - 360
            else -> measure
        }.let {
            (it + .2F * (old - it) + 360) % 360
        }

        private fun exponentialSmoothing180(old: Float, measure: Float): Float = when {
            old - measure > 180 -> measure + 360
            measure - old > 180 -> measure - 360
            else -> measure
        }.let {
            (it + 0.2F * (old - it) + 540) % 360 - 180
        }

    }


}
