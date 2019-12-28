package com.wust.ssd.fitnessclubfinder.ui.camera

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.MainRenderer
import com.wust.ssd.fitnessclubfinder.di.Injectable
import javax.inject.Inject

class CameraFragment : Fragment(), Injectable {

    private val TAG = this.javaClass.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var renderer:MainRenderer

    private var viewModel: CameraViewModel? = null

    private lateinit var mSurfaceView: GLSurfaceView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel == null)
            viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(CameraViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_camera, container, false)
        val textView: TextView = root.findViewById(R.id.text_camera)
        viewModel?.text?.observe(this, Observer {
            textView.text = it
        })
        mSurfaceView = root.findViewById(R.id.surface_view)

        mSurfaceView.apply {
            preserveEGLContextOnPause = true
            preserveEGLContextOnPause = true
            setEGLContextClientVersion(2)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            setRenderer(renderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

        return root
    }


    override fun onPause() {
        super.onPause()
        mSurfaceView.onPause()
        renderer.onPause()

    }


    override fun onResume() {
        super.onResume()
        renderer.onResume()
        mSurfaceView.onResume()
    }
}