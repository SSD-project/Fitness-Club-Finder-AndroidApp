package com.wust.ssd.fitnessclubfinder

import androidx.test.platform.app.InstrumentationRegistry
import com.wust.ssd.fitnessclubfinder.common.ARMarker
import com.wust.ssd.fitnessclubfinder.common.MarkerPositionHelper
import com.wust.ssd.fitnessclubfinder.common.model.Club
import com.wust.ssd.fitnessclubfinder.common.model.Geometry
import com.wust.ssd.fitnessclubfinder.utils.Fixtures
import org.junit.Test

import org.junit.Assert.*

private val helper: MarkerPositionHelper = MarkerPositionHelper(60F, 60F)
private val appContext = InstrumentationRegistry.getInstrumentation().targetContext


class MarkerPositionHelperTest {
    @Test
    fun horizontal_position_forward_isCorrect() {

        var expected:Float = 0.5F

        var actual :Float = helper.getHorizontalPosition(0F, 0F)

        assertEquals("Positioning marker on screen failed", expected, actual, 0.05f)
    }
    @Test
    fun horizontal_position_subedge_isCorrect() {

        var f: Fixtures = Fixtures()
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
    @Test
    fun minimal_boundary_isCorrect(){
        var clubs: List<Club> = Fixtures().prepareAPIResult().results
        var mrs: ArrayList<ARMarker> = ArrayList()
        assertTrue("Cannot find any clubs", clubs.count() > 0)

        for( d in 1..5)
        {
            var m: ARMarker = ARMarker(appContext, clubs[0])
            m.distance =  d.toFloat()
            mrs.add(m)

        }
        var expected = 1
        var actual = helper.getBoundaryDistances(false, mrs)
        assertEquals("Finding boundary minimal distance failed", actual, expected)

    }
    @Test
    fun maximal_boundary_isCorrect(){
        var clubs: List<Club> = Fixtures().prepareAPIResult().results
        var mrs: ArrayList<ARMarker> = ArrayList()
        assertTrue("Cannot find any clubs", clubs.count() > 0)

        for( d in 1..5)
        {
            var m: ARMarker = ARMarker(appContext, clubs[0])
            m.distance =  d.toFloat()
            mrs.add(m)

        }
        var expected = 5
        var actual = helper.getBoundaryDistances(false, mrs)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }
    @Test
    fun minimal_vertical_position_isCorrect(){

        var expected = -80F
        var actual = helper.getMinVerticalPosition(1F, 10F, 100F)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }

    @Test
    fun minimal_vertical_position_border_isCorrect(){

        var expected = 10F
        var actual = helper.getMinVerticalPosition(0F, 10F, 100F)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }

    @Test
    fun minimal_vertical_position_pastborder_isCorrect(){

        var expected = 10F
        var actual = helper.getMinVerticalPosition(-1F, 10F, 100F)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }

    @Test
    fun maximal_vertical_position_isCorrect(){

        var expected = 10F
        var actual = helper.getMaxVerticalPosition(1F, 10F, 100F)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }

    @Test
    fun maximal_vertical_position_border_isCorrect(){

        var expected = 100F
        var actual = helper.getMaxVerticalPosition(0F, 10F, 100F)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }

    @Test
    fun maximal_vertical_position_pastborder_isCorrect(){

        var expected = 100F
        var actual = helper.getMaxVerticalPosition(-1F, 10F, 100F)
        assertEquals("Finding boundary maximal distance failed", actual, expected)

    }

    @Test
    fun margin_top_isCorrect(){

        var expected = -8800‬
        var actual = helper.getMarginTop(100F, 200F, 10F)
        assertEquals("Finding margin top failed", actual, expected)

    }

    @Test
    fun tilt_isCorrect(){

        var expected = 45
        var actual = helper.getTilt(1F)
        assertEquals("Finding tilt failed", actual, expected)

    }


}
