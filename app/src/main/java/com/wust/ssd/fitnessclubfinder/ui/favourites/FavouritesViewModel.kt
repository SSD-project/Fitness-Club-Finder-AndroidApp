package com.wust.ssd.fitnessclubfinder.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavouritesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favourites Fragment"
    }
    val text: LiveData<String> = _text
}