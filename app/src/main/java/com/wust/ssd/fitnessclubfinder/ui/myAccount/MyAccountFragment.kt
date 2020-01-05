package com.wust.ssd.fitnessclubfinder.ui.myAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wust.ssd.fitnessclubfinder.R

class MyAccountFragment : Fragment() {

    private lateinit var myAccountViewModel: MyAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myAccountViewModel =
            ViewModelProviders.of(this).get(MyAccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_account, container, false)
        val textView: TextView = root.findViewById(R.id.text_my_account)
        myAccountViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}