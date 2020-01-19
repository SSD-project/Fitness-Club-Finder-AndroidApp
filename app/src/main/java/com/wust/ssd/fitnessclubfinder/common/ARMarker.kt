package com.wust.ssd.fitnessclubfinder.common

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.wust.ssd.fitnessclubfinder.R
import com.wust.ssd.fitnessclubfinder.common.model.Club

class ARMarker(val context: Context, val club: Club) {

    companion object{
        const val MARKER_DEFAULT ="marker_background"
        const val ARROW_LEFT = "ic_round_arrow_left"
        const val ARROW_RIGHT = "ic_round_arrow_right"
    }

    var marginTop: Float = 0F//from 0F to ~1800F
    var marginLeft: Float = 0F//from 0F to ~1000F
    var icon = MARKER_DEFAULT
    var currentIcon = ""

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
            (marginLeft - this.width / 2).toInt(),
            (marginTop - this.height / 2).toInt(),
            -this.width / 2,
            -this.height
        )
        //        view.measure()
    }


}