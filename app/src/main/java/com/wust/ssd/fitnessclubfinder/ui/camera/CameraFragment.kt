package com.wust.ssd.fitnessclubfinder.ui.camera

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.di.Injectable
import javax.inject.Inject

class CameraFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: CameraViewModel? = null

    private lateinit var cameraViewModel: CameraViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel == null) {
            viewModel =
                ViewModelProviders.of(this, viewModelFactory).get(CameraViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        cameraViewModel =
//            ViewModelProviders.of(this).get(CameraViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val textView: TextView = root.findViewById(R.id.text_camera)
//        cameraViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

//        val surfaceView: GLSurfaceView = root.findViewById(R.id.surfaceView)

        return root
    }


}