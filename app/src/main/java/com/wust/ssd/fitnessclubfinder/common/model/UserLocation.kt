package com.wust.ssd.fitnessclubfinder.common.model

import android.location.Location

data class UserLocation(val latitude: Double, val longitude: Double) {
    constructor(location: Location): this(location.latitude, location.longitude)
}