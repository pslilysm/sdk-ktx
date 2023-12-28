package per.pslilysm.sdk_library.extention

import android.graphics.RectF
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Extension for math
 *
 * @author pslilysm
 * Created on 2023/06/29 14:59
 * @since 2.2.0
 */

/**
 * Calculate angle between two points
 *
 * @param another Second point
 * @return Calculated angle
 */
fun Pair<Float, Float>.calculateAngle(another: Pair<Float, Float>): Float {
    var angle = Math.toDegrees(atan2((this.second - another.second).toDouble(), (this.first - another.first).toDouble())).toFloat()
    if (angle < 0) {
        angle += 360f
    }
    return angle
}

/**
 * Calculate the point is in the rect
 *
 * @return True if the point in the rect
 */
fun Pair<Float, Float>.isInRect(pointList: List<Pair<Float, Float>>): Boolean {
    val n = pointList.size //n=4
    assert(n == 4)
    var sum = 0f
    for (i in 0 until 3) {
        sum += arrayListOf(pointList[i].first to pointList[i].second, pointList[i + 1].first to pointList[i + 1].second, this).calculateArea()
    }
    sum += arrayListOf(pointList[n - 1].first to pointList[n - 1].second, pointList[0].first to pointList[0].second, this).calculateArea()
    val polyArea = 2 * arrayListOf(pointList[0].first to pointList[0].second, pointList[1].first to pointList[1].second, pointList[2].first to pointList[2].second).calculateArea()
    return sum <= polyArea
}

/**
 * Calculate the area by 3 points
 *
 * @return Calculated area
 */
fun List<Pair<Float, Float>>.calculateArea(): Float {
    assert(this.size == 3)
    return 0.5f * abs((this[1].first - this[0].first) * (this[2].second - this[0].second) - (this[2].first - this[0].first) * (this[1].second - this[0].second))
}

/**
 * Calculate the 4 points after the rect is scaled and rotated
 *
 * @param scale From 0 to max
 * @param rotate From -360 to 360
 * @return Calculated 4 points
 */
fun RectF.calculatePointsWithScaleRotate(scale: Float, rotate: Float): List<Pair<Float, Float>> {
    var rotateFix = rotate
    if (rotateFix < 0) {
        rotateFix += 360f
    }
    val radians = Math.toRadians(rotateFix.toDouble())
    // center point
    val center = left + width() / 2 to top + height() / 2
    // half width and half height after scale
    val hw = width() * scale / 2
    val hh = height() * scale / 2
    var diffX = (hw * cos(radians)).toFloat()
    var diffY = (hw * sin(radians)).toFloat()
    val lvc = center.first - diffX to center.second - diffY
    val rvc = center.first + diffX to center.second + diffY
    diffX = (hh * sin(radians)).toFloat()
    diffY = (hh * cos(radians)).toFloat()
    val tl = lvc.first + diffX to lvc.second - diffY
    val tr = rvc.first + diffX to rvc.second - diffY
    val bl = lvc.first - diffX to lvc.second + diffY
    val br = rvc.first - diffX to rvc.second + diffY
    return arrayListOf(tl, tr, bl, br)
}