package com.wust.ssd.fitnessclubfinder.ui.searchClub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchClubViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Search club Fragment"
    }
    val text: LiveData<String> = _text
}