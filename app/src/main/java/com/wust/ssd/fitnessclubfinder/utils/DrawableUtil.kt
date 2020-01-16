package com.wust.ssd.fitnessclubfinder.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat

class DrawableUtil(val context: Context) {
    private fun convertDpToPixel(dp: Float) =
        (dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toInt()

    fun importResource(res: String, c: Class<out Any>, heightDp: Int, widthDp: Int) =
        getResourceId(res, c)?.let {
            convertToBitmapDrawable(it, heightDp, widthDp)
        }

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