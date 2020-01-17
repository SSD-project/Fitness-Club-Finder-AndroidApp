package com.wust.ssd.fitnessclubfinder.common

class MarkerPositionHelper(
    private val horizontalViewAngle: Float,
    private val verticalViewAngle: Float
) {

    fun getHorizontalPosition(alpha: Float, bearing: Float): Float {

        val x = when {
            bearing - alpha > 180 -> alpha + 360
            alpha - bearing > 180 -> alpha - 360
            else -> alpha
        }
        return ((x - bearing) / horizontalViewAngle!! + .5f)
    }


    fun getVerticalParallax(pitch: Float): Float =
        (2 * (90 + pitch % 360) / verticalViewAngle!!).let {
            when {
                it < -1 -> -1f
                it > 1 -> 1f
                else -> it
            }
        }

    fun getBoundaryDistances(maximizing: Boolean, markers: List<ARMarker>): Double {
        var result = (-1f).toDouble()
        markers.forEach { marker ->
            marker.distance?.let {
                if (result < 0 || maximizing && it > result || !maximizing && it < result)
                    result = it.toDouble()
            }

        }
        return result
    }

    fun getMinVerticalPosition(parallax: Float, minY: Float, maxY: Float) =
        if (parallax < 0) minY + parallax * (minY - maxY) else minY

    fun getMaxVerticalPosition(parallax: Float, minY: Float, maxY: Float) =
        if (parallax > 0) maxY + parallax * (minY - maxY) else maxY

    fun getMarginTop(y: Float, max: Float, min: Float) = max - y * (max - min)
}