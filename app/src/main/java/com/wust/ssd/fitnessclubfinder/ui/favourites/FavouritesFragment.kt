package com.wust.ssd.fitnessclubfinder.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wust.ssd.fitnessclubfinder.R

class FavouritesFragment : Fragment() {

    private lateinit var favouritesViewModel: FavouritesViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouritesViewModel =
            ViewModelProviders.of(this).get(FavouritesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)
        val textView: TextView = root.findViewById(R.id.text_favourites)
        favouritesViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }



}