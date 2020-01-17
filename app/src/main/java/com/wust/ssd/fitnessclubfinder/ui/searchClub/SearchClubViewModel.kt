package com.wust.ssd.fitnessclubfinder.ui.searchClub

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wust.ssd.fitnessclubfinder.R

class SearchClubViewModel : ViewModel() {



    private val _text = MutableLiveData<String>().apply {
        value = "This is Search club Fragment"
    }
    val text: LiveData<String> = _text


}