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

class ARMarker(val context: Context, val club: Club) {

    var verticalPosition: Float = 1800F//from 0F to ~1800F
    var horizontalPosition: Float = 1000F//from 0F to ~1000F

    val view: ImageView = ImageView(context).apply {
        visibility = View.VISIBLE
    }

    init {
        view.setOnClickListener {
            Log.e("ARMarker", "CLICK")
        }
    }

    fun refresh() {
        setupBackground()
//        view.visibility
//        view.alpha
//        view.elevation
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
//        val size = convertDpToPixel(iconSize, context)
        //height and width should depend on the iconSize
//        layoutParams.height
//        layoutParams.width
        layoutParams.setMargins(
            (horizontalPosition - layoutParams.width / 2).toInt(),
            (verticalPosition - layoutParams.height / 2).toInt(),
            -layoutParams.width / 2,
            -layoutParams.height
        )
        view.layoutParams = layoutParams
        view.requestLayout()
    }

    private fun setupBackground() {
        val drawableId = getResourceId("marker_background", drawable::class.java)
        val maxSize = 64

        view.background = drawableId?.let { convertToBitmapDrawable(it, maxSize, maxSize) }
    }

    private fun convertDpToPixel(dp: Float) =
        (dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toInt()

    private fun getResourceId(res: String, c: Class<out Any>): Int? {
        try {
            val field = c.getDeclaredField(res)
            return field.getInt(field)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun convertToBitmapDrawable(
        drawableId: Int,
        heightDp: Int,
        widthDp: Int
    ): BitmapDrawable? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        drawable?.let { draw ->
            val widthPixel = convertDpToPixel(widthDp.toFloat())
            val heightPixel = convertDpToPixel(heightDp.toFloat())
            val bitmap: Bitmap = convertToBitmap(draw, widthPixel, heightPixel)
            return BitmapDrawable(context.resources, bitmap)
        }
        return null
    }

    private fun convertToBitmap(
        drawable: Drawable,
        widthPixel: Int,
        heightPixel: Int
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(widthPixel, heightPixel, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.bounds.apply {
            left = 0
            top = 0
            right = canvas.width
            bottom = canvas.height
        }
        drawable.draw(canvas)
        return bitmap

    }
}