package com.wust.ssd.fitnessclubfinder.ui.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wust.ssd.fitnessclubfinder.common.model.Club
import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import com.wust.ssd.fitnessclubfinder.common.model.UserLocation
import com.wust.ssd.fitnessclubfinder.common.repository.NearbyClubsObserver
import com.wust.ssd.fitnessclubfinder.common.repository.NearbySearchRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.math.abs

class CameraViewModel @Inject constructor(val nearbySearchRepository: NearbySearchRepository) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is camera Fragment"
    }
    private var disposable = CompositeDisposable()
    val location = MutableLiveData<UserLocation>()
    val nearbyClubs = MutableLiveData<List<Club>>()
    val text: LiveData<String> = _text

    fun requestUserLocationUpdates() {
        disposable.add(nearbySearchRepository.subscribe(DataObserver()))
    }

    private fun userLocationUpdate(data: UserLocation) = location.postValue(data)

    private fun nearbyClubsUpdate(data: List<Club>) = nearbyClubs.postValue(data)

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
            if (t.results.isNotEmpty())
                nearbyClubsUpdate(t.results)
        }

    }

    fun createBitmapWithClubs(displayMetrics: DisplayMetrics, previewSize:Size): Bitmap =
        createCircle(previewSize, 2000F, 2000F, displayMetrics)

    private fun createCircle(
        size: Size,
        x: Float,
        y: Float,
        displayMetrics: DisplayMetrics
    ): Bitmap {
        val overlay = Bitmap.createBitmap(
            size.width,
            size.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(overlay)


        val paint = Paint().apply {
            color = Color.GREEN

        }
        val textPaint = TextPaint().apply {
            textSize = (45 * displayMetrics.density)
            isAntiAlias = true
            style = Paint.Style.FILL_AND_STROKE
            color = Color.MAGENTA

        }
        canvas.drawCircle(x, y, 50F, paint)
        canvas.drawText("Super Silownia!", x, y, textPaint)

        return overlay
    }
}
