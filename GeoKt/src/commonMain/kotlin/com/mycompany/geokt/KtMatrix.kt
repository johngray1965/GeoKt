package com.mycompany.geokt

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class KtMatrix {
    val values = FloatArray(9)

    init {
        reset()
    }

    fun reset() {
        values.fill(0f)
        values[0] = 1f
        values[4] = 1f
        values[8] = 1f
    }

    fun set(other: KtMatrix) {
        other.values.copyInto(values)
    }

    fun mapPoints(points: FloatArray) {
        val count = points.size / 2
        for (i in 0 until count) {
            val x = points[i * 2]
            val y = points[i * 2 + 1]
            points[i * 2] = values[0] * x + values[1] * y + values[2]
            points[i * 2 + 1] = values[3] * x + values[4] * y + values[5]
        }
    }

    fun postTranslate(dx: Float, dy: Float): Boolean {
        val temp = KtMatrix()
        temp.setTranslate(dx, dy)
        postConcat(temp)
        return true
    }

    fun setTranslate(dx: Float, dy: Float) {
        reset()
        values[2] = dx
        values[5] = dy
    }

    fun postScale(sx: Float, sy: Float, px: Float, py: Float): Boolean {
        val temp = KtMatrix()
        temp.setScale(sx, sy, px, py)
        postConcat(temp)
        return true
    }

    fun postScale(sx: Float, sy: Float): Boolean {
        val temp = KtMatrix()
        temp.setScale(sx, sy)
        postConcat(temp)
        return true
    }

    fun setScale(sx: Float, sy: Float, px: Float, py: Float) {
        reset()
        values[0] = sx
        values[4] = sy
        values[2] = px - sx * px
        values[5] = py - sy * py
    }

    fun setScale(sx: Float, sy: Float) {
        reset()
        values[0] = sx
        values[4] = sy
    }

    fun postRotate(degrees: Float, px: Float, py: Float): Boolean {
        val temp = KtMatrix()
        temp.setRotate(degrees, px, py)
        postConcat(temp)
        return true
    }

    fun postRotate(degrees: Float): Boolean {
        val temp = KtMatrix()
        temp.setRotate(degrees)
        postConcat(temp)
        return true
    }

    fun setRotate(degrees: Float, px: Float, py: Float) {
        reset()
        val radians = Math.toRadians(degrees.toDouble())
        val sin = kotlin.math.sin(radians).toFloat()
        val cos = kotlin.math.cos(radians).toFloat()
        values[0] = cos
        values[1] = -sin
        values[2] = px - px * cos + py * sin
        values[3] = sin
        values[4] = cos
        values[5] = py - px * sin - py * cos
    }

    fun setRotate(degrees: Float) {
        reset()
        val radians = Math.toRadians(degrees.toDouble())
        val sin = kotlin.math.sin(radians).toFloat()
        val cos = kotlin.math.cos(radians).toFloat()
        values[0] = cos
        values[1] = -sin
        values[3] = sin
        values[4] = cos
    }

    fun postConcat(other: KtMatrix): Boolean {
        val result = KtMatrix()
        for (i in 0..2) {
            for (j in 0..2) {
                var sum = 0f
                for (k in 0..2) {
                    sum += values[i * 3 + k] * other.values[k * 3 + j]
                }
                result.values[i * 3 + j] = sum
            }
        }
        set(result)
        return true
    }

    fun getRotation(): Float {
        val p = floatArrayOf(0f, 1f, 0f, 0f)
        mapPoints(p)
        val r = atan2((p[1] - p[3]).toDouble(), (p[0] - p[2]).toDouble())
        return Math.toDegrees(r).toFloat()
    }

    fun getTranslationX(): Float = values[2]
    fun getTranslationY(): Float = values[5]
    fun getScaleX(): Float = sqrt(values[0] * values[0] + values[3] * values[3])
    fun getScaleY(): Float = sqrt(values[1] * values[1] + values[4] * values[4])
}
