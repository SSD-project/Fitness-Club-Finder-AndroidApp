package com.wust.ssd.fitnessclubfinder.utils

import android.graphics.Point
import android.view.WindowManager

class WindowSizeUtil(private val windowManager: WindowManager) {

    fun getWindowHeight(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }


    fun getWindowWidth(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

}