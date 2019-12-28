package com.wust.ssd.fitnessclubfinder.ui.camera

import android.graphics.HardwareRenderer
import android.graphics.Point
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.ar.core.*
import com.google.ar.sceneform.ux.ArFragment
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.BackgroundRenderer
import com.wust.ssd.fitnessclubfinder.common.CameraPermissionHelper
import com.wust.ssd.fitnessclubfinder.common.DisplayRotationHelper
import com.wust.ssd.fitnessclubfinder.common.PointerDrawable
import com.wust.ssd.fitnessclubfinder.di.Injectable
import java.lang.Exception
import javax.inject.Inject
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt

class CameraFragment : Fragment(), Injectable, GLSurfaceView.Renderer {

    private val TAG = this.javaClass.simpleName

    @Inject
    lateinit var displayRotationHelper: DisplayRotationHelper

    @Inject
    lateinit var backgroundRenderer: BackgroundRenderer

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: CameraViewModel? = null

    private var mSession: Session? = null
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
            setRenderer(this@CameraFragment)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

        return root
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        if (mSession == null) return
        displayRotationHelper.updateSessionIfNeeded(mSession)
        mSession!!.setCameraTextureName(backgroundRenderer.textureId)
        val frame = mSession!!.update()
        backgroundRenderer.draw(frame)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        displayRotationHelper.onSurfaceChanged(width, height)
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        backgroundRenderer.createOnGlThread()


    }


    override fun onPause() {
        super.onPause()
        mSession?.let {
            displayRotationHelper.onPause()
            mSurfaceView.onPause()
            it.pause()
        }


    }

    override fun onResume() {
        super.onResume()
        if (mSession === null)
            mSession = Session(context)

        mSession!!.resume()
        mSurfaceView.onResume()
        displayRotationHelper.onResume()
    }
}