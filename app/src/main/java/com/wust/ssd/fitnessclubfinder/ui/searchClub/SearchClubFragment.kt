package com.wust.ssd.fitnessclubfinder.ui.searchClub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wust.ssd.fitnessclubfinder.R

class SearchClubFragment : Fragment() {



    private lateinit var searchClubViewModel: SearchClubViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchClubViewModel =
            ViewModelProviders.of(this).get(SearchClubViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search_club, container, false)
        val textView: TextView = root.findViewById(R.id.text_search_club)
        searchClubViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}