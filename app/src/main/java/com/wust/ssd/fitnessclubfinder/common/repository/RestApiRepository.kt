package com.wust.ssd.fitnessclubfinder.common.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.wust.ssd.fitnessclubfinder.common.model.NearbySearchAPIResult
import com.wust.ssd.fitnessclubfinder.common.network.NearbySearchService
import com.wust.ssd.fitnessclubfinder.common.network.RetrofitFactory
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class RestApiRepository
@Inject constructor(retrofitFactory: RetrofitFactory) {


    private val nearbySearchService = retrofitFactory.createService(
        NearbySearchService.API_URL,
        NearbySearchService::class.java
    )

    /**
     * Check if network is available
     * @param context required to retrieve ConnectivityService
     * @return boolean - network status
     */
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    fun getNearbyLocations(lat: Double, long: Double): Observable<NearbySearchAPIResult> {
        Log.e(lat.toString(), long.toString())
        return nearbySearchService.getNearbyFitnessClubs(
            String.format(
                Locale.US,
                "%f,%f",
                lat,
                long
            ),
            radius = "1000",
            type = "gym",
            key= NearbySearchService.API_KEY
        )
    }
}
