package com.wust.ssd.fitnessclubfinder.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wust.ssd.fitnessclubfinder.common.ARMarker
import com.wust.ssd.fitnessclubfinder.common.model.Club
import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import com.wust.ssd.fitnessclubfinder.common.model.UserLocation
import com.wust.ssd.fitnessclubfinder.common.repository.NearbyClubsObserver
import com.wust.ssd.fitnessclubfinder.common.repository.NearbySearchRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
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
    val location = MutableLiveData<UserLocation>()
    val nearbyClubs = MutableLiveData<List<Club>>()
    val markers = MutableLiveData<List<ARMarker>>()
    val text: LiveData<String> = _text

    val clubs = mutableListOf<ARMarker>()

    fun requestUserLocationUpdates() {
        disposable.add(nearbySearchRepository.subscribe(DataObserver()))
    }

    private fun userLocationUpdate(data: UserLocation) = location.postValue(data)

    private fun nearbyClubsUpdate(data: List<Club>) = nearbyClubs.postValue(data)

    private fun markersUpdate(data: List<ARMarker>) = markers.postValue(data)

    fun onLocationChanged(userLocation: Location) =
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


    private fun getVerticalPosition(alpha: Double, bearing: Float): Float {

        val x = when {
            bearing - alpha > 180 -> alpha + 360
            alpha - bearing > 180 -> alpha - 360
            else -> alpha
        }
        return ((x - bearing) /  /*verticalAngle*/ +.5f).toFloat()
    }

}
