package com.wust.ssd.fitnessclubfinder.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.ar.sceneform.ux.ArFragment
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.di.Injectable
import javax.inject.Inject

class CameraFragment : Fragment(), Injectable {

    private val TAG = "CAMERA_FRAGMENT"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: CameraViewModel? = null

    private lateinit var arFragment: ArFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel == null) {
            viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(CameraViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_camera, container, false)

        val textView: TextView = root.findViewById(R.id.text_camera)
        textView.text = "Camera fragment"
//        cameraViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

        arFragment = childFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment
        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            arFragment.onUpdate(frameTime)
        }
        return root
    }


}