package com.wust.ssd.fitnessclubfinder.di

import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wust.ssd.fitnessclubfinder.ui.camera.CameraViewModel
import com.wust.ssd.fitnessclubfinder.ui.searchClub.SearchClubViewModel
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton


@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory
@Inject
constructor(viewModelComponent: ViewModelComponent)
    : ViewModelProvider.Factory {

    private val creators: ArrayMap<Class<*>, Callable<out ViewModel>> = ArrayMap()

    init {
        creators[CameraViewModel::class.java] = Callable { viewModelComponent.cameraViewModel() }
        creators[SearchClubViewModel::class.java] = Callable { viewModelComponent.searchClubViewModel() }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Callable<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) throw IllegalArgumentException("Model class not found$modelClass")
        try {
            return creator.call() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

}