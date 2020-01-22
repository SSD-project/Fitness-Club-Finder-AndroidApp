package com.wust.ssd.fitnessclubfinder

import com.wust.ssd.fitnessclubfinder.common.ARMarker
import com.wust.ssd.fitnessclubfinder.common.MarkerPositionHelper
import org.junit.Test

import org.junit.Assert.*

private val helper: MarkerPositionHelper = MarkerPositionHelper(60F, 60F)
//private val markers = listOf<ARMarker>( ARMarker(null, null), ... )
//                          ???

class MarkerPositionHelperTest {
    @Test
    fun horizontal_position_forward_isCorrect() {

        var expected:Float = 0.5F

        var actual :Float = helper.getHorizontalPosition(0F, 0F)

        assertEquals("Positioning marker on screen failed", expected, actual, 0.05f)
    }
    @Test
    fun horizontal_position_subedge_isCorrect() {

        var expected:Float = 0.95F

        var actual :Float = helper.getHorizontalPosition(0F, 29F)

        assertEquals("Positioning marker before the edge of the screen failed", expected, actual, 0.05f)
    }
    @Test
    fun horizontal_position_pastedge_isCorrect() {

        var expected:Float = 1.05F

        var actual :Float = helper.getHorizontalPosition(0F, 31F)

        assertEquals("Positioning marker past the edge of the screen failed", expected, actual, 0.05f)
    }

    @Test
    fun parallax_forward_isCorrect() {

        var expected:Float = 0F

        var actual :Float = helper.getVerticalParallax(-90F)

        assertEquals("Parallax marker on screen failed", expected, actual, 0.05f)
    }
    @Test
    fun parallax_subedge_isCorrect() {

        var expected:Float = -0.98F

        var actual :Float = helper.getVerticalParallax(-115F)

        assertEquals("Parallax marker before the edge of the screen failed", expected, actual, 0.05f)
    }
    @Test
    fun parallax_pastedge_isCorrect() {

        var expected:Float = -1F

        var actual :Float = helper.getVerticalParallax(-125F)

        assertEquals("Parallax past the edge of the screen failed", expected, actual, 0.01f)
    }


}
