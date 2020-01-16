package com.wust.ssd.fitnessclubfinder.ui.camera

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wust.ssd.fitnessclubfinder.common.ARMarker
import com.wust.ssd.fitnessclubfinder.common.model.Club
import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import com.wust.ssd.fitnessclubfinder.common.repository.NearbyClubsObserver
import com.wust.ssd.fitnessclubfinder.common.repository.NearbySearchRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.concurrent.thread
import kotlin.math.abs

class CameraViewModel @Inject constructor(
    private val context: Context,
    val nearbySearchRepository: NearbySearchRepository
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

    private fun getVerticalParallax(altitude: Float) =
//altitude (probably pitch) should be in degrees
        screenHeight!! - (1 * (altitude + 90) / verticalViewAngle!! + 0.5f).let {
            when {
                it < -1 -> -1f
                it > 1 -> 1f
                else -> it
            }
        } * screenHeight!!

    private fun countBearingsAndDistances(markers: List<ARMarker>, location: Location) {
        markers.forEach {
            val loc = Location(it.club.name)
            loc.latitude = it.club.geometry.location.lat
            loc.longitude = it.club.geometry.location.lng
            it.bearing = location.bearingTo(loc)
            it.distance = location.distanceTo(loc)
        }
    }

    private fun getHorizontalPosition(alpha: Float, bearing: Float): Float {

        val x = when {
            bearing - alpha > 180 -> alpha + 360
            alpha - bearing > 180 -> alpha - 360
            else -> alpha
        }



        return ((x - bearing) / horizontalViewAngle!! + .5f) * screenWidth!!
    }

    private fun getVerticalPosizion(distance: Float, max: Double, min: Double): Float {
        return ((distance - min) / (max - min)).toFloat() * screenHeight?.toFloat()!!
    }

    fun runWorker(
        markers: List<ARMarker>
        , activity: Activity
    ) {




        thread(start = true) {
            while (
                true
            ) {

                if (location.value !== null &&
                    compass.azimuth !== null &&
                    compass.pitch !== null &&
                    compass.roll !== null
                ) {
                    countBearingsAndDistances(markers, location.value!!)

                    markers.map {

                        it.horizontalPosition =
                            getHorizontalPosition(it.bearing!!, compass.azimuth!!)

                        it.verticalPosition = getVerticalParallax(compass.pitch!!)

                        val layoutParams = it.refresh()


                        activity.runOnUiThread {
                            it.view.layoutParams = layoutParams
                            it.view.requestLayout()
                        }
                    }

                }else Thread.sleep(1000)
                Thread.sleep(33)
            }
        }
    }

    private fun getBoundaryDistances(maximizing: Boolean, markers: List<ARMarker>): Double {
        var result = (-1f).toDouble()
        markers.forEach { marker ->
            marker.distance?.let {
                if (result < 0 || maximizing && it > result || !maximizing && it < result)
                    result = it.toDouble()
            }

        }
        return result

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

        var azimuth: Float? = null
        var pitch: Float? = null
        var roll: Float? = null

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
                azimuth =
                    azimuth?.let { exponentialSmoothing360(it, e.values[0]) } ?:
                            e.values[0]
                pitch = pitch?.let { exponentialSmoothing180(it, e.values[1]) } ?:
                        e.values[1]
                roll = roll?.let { exponentialSmoothing180(it, e.values[2]) } ?:
                        e.values[2]

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
        }.apply {
            (this + .2F * (old - this) + 360) % 360
        }

        private fun exponentialSmoothing180(old: Float, measure: Float): Float = when {
            old - measure > 180 -> measure + 360
            measure - old > 180 -> measure - 360
            else -> measure
        }.apply {
            (this + .2F * (old - this) + 540) % 360 - 180
        }

    }


}
