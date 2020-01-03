package com.wust.ssd.fitnessclubfinder.common.repository

import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import io.reactivex.observers.DisposableObserver

abstract class NearbyClubsObserver : DisposableObserver<NearbySearchAPIResult>()