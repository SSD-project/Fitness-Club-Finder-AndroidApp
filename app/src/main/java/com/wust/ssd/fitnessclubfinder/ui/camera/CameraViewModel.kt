package com.wust.ssd.fitnessclubfinder.ui.camera

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
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

    fun onLocationChanged(userLocation: Location) {
        nearbySearchRepository.lastLocation = userLocation
        nearbySearchRepository.next()
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
}
