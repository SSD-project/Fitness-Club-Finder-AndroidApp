package com.wust.ssd.fitnessclubfinder.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.wust.ssd.fitnessclubfinder.R.drawable
import com.wust.ssd.fitnessclubfinder.common.model.Club
import android.location.Location

class ARMarker(val context: Context, val club: Club) {

    var verticalPosition: Float = 0F//from 0F to ~1800F
    var horizontalPosition: Float = 0F//from 0F to ~1000F

    var distance: Float? = null
    var bearing: Float? = null

    val view: ImageView = ImageView(context).apply {
        visibility = View.VISIBLE
    }

    init {
        view.setOnClickListener {
            Log.e("ARMarker", "CLICK")
        }
    }

    fun refresh() = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(
            (horizontalPosition - this.width / 2).toInt(),
            (verticalPosition - this.height / 2).toInt(),
            -this.width / 2,
            -this.height
        )
        //        view.measure()
    }


}