package com.wust.ssd.fitnessclubfinder.common.repository

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.wust.ssd.fitnessclubfinder.common.model.Club
import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import com.wust.ssd.fitnessclubfinder.utils.Fixtures
import com.wust.ssd.fitnessclubfinder.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Singleton
import kotlin.concurrent.timerTask

@Singleton
class NearbySearchRepository(
    private val restApiRepository: RestApiRepository,
    private val context: Context,
    private val schedulers: RxSchedulersFacade
) {
    private val stream: BehaviorSubject<NearbySearchAPIResult> = BehaviorSubject.create()
    private val disposable = CompositeDisposable()
    var lastLocation: Location? = null
    lateinit var nearbyClubs: List<Club>

    private var apiCallLockFlag = true
    private var apiCallInProgress = false
    var positionChangeFlag = true
    private var apiFailFlag = false
    private var fitnessClubs: List<Club>? = null
    private var apiTimer: Timer? = null
    private var apiCallInterval: Long = 6
    private var apiCallsOn = false

    private fun callForNearbyClubsUpdate(data: Location) {

        if (restApiRepository.isInternetAvailable(context))
            disposable.add(
                restApiRepository
                    .getNearbyLocations(
                        data.latitude,
                        data.longitude
                    )
                    .subscribeOn(schedulers.computation())
                    .observeOn(schedulers.computation())
                    .subscribe({ result ->
                        if(result.results.isNotEmpty())
                        handleNearbyClubsUpdate(result)
                        else handleNearbyClubsUpdate(Fixtures().prepareAPIResult())
                    },
                        { error -> throw error })
            )


    }

    private fun handleNearbyClubsUpdate(clubs: NearbySearchAPIResult?) =
        clubs?.results?.let {
            if (it.isNotEmpty()) {
                apiCallInProgress = false
                fitnessClubs = it
                next()
            }
        }


    fun startNearbyClubsApiCalls() {
        apiTimer = Timer()
        apiTimer?.scheduleAtFixedRate(
            timerTask {
                if (!apiCallInProgress) apiCallLockFlag = false//ATTENTION: lock for timer
            }, 0, apiCallInterval * 1000
        )
//        apiCallsOn = true//TODO: turn on if you want to query in period of time
    }

    fun stopNearbyClubsApiCalls() {
        if (apiTimer != null) {
            apiTimer!!.cancel()
            apiTimer!!.purge()
        }
        apiCallsOn = false
        apiCallInProgress = false
        positionChangeFlag = true
    }


    fun subscribe(observer: NearbyClubsObserver): Disposable =
        stream.subscribeOn(schedulers.io()).subscribeWith(observer)

    fun clearDisposable() = disposable.clear()

    fun isNearbyClubsInit(): Boolean = ::nearbyClubs.isInitialized


    fun next() {
        if (!apiCallLockFlag && positionChangeFlag && lastLocation !== null) {
            callForNearbyClubsUpdate(lastLocation!!)
            positionChangeFlag = false
            apiCallLockFlag = true

        }
        fitnessClubs?.let {
            stream.onNext(NearbySearchAPIResult(emptyList(), it))
        }


    }

    fun stopApiCalls() {
        apiCallInProgress = false
        positionChangeFlag = true
    }

}