package com.wust.ssd.fitnessclubfinder.common

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.google.ar.core.Session
import javax.inject.Inject
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainRenderer
@Inject constructor(
    private val context: Context,
    private val displayRotationHelper: DisplayRotationHelper,
    private val backgroundRenderer: BackgroundRenderer
) : GLSurfaceView.Renderer {


    private var mSession: Session? = null

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        if (mSession == null) return
        displayRotationHelper.updateSessionIfNeeded(mSession)
        mSession!!.setCameraTextureName(backgroundRenderer.textureId)
        val frame = mSession!!.update()
        backgroundRenderer.draw(frame)
    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        displayRotationHelper.onSurfaceChanged(width, height)
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        backgroundRenderer.createOnGlThread()
    }

    fun onPause() {
        mSession?.let {
            displayRotationHelper.onPause()
            it.pause()
        }
    }

    fun onResume() {
        if (mSession === null)
            mSession = Session(context)

        mSession!!.resume()
        displayRotationHelper.onResume()
    }

}