package com.wust.ssd.fitnessclubfinder.ui.myAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyAccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is MyAccount Fragment"
    }
    val text: LiveData<String> = _text
}