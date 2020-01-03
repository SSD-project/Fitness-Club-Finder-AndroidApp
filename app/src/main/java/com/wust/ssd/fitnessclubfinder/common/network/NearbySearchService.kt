package com.wust.ssd.fitnessclubfinder.common.network

import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbySearchService {

    @GET("place/nearbysearch/json")
    fun getNearbyFitnessClubs(
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("key") key: String
        ): Observable<NearbySearchAPIResult>

    companion object {
        const val API_URL = "https://maps.googleapis.com/maps/api/"
        const val API_KEY = ""

    }
}