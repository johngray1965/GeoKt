package com.mycompany.geokt

import kotlin.math.roundToInt

class KtRectF(var left: Float, var top: Float, var right: Float, var bottom: Float) {
    val width: Float
        get() = right - left
    val height: Float
        get() = bottom - top

    fun set(left: Float, top: Float, right: Float, bottom: Float) {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }

    fun roundOut(dst: KtRect) {
        dst.set(
            floor(left + 0.5f).roundToInt(),
            floor(top + 0.5f).roundToInt(),
            floor(right + 0.5f).roundToInt(),
            floor(bottom + 0.5f).roundToInt()
        )
    }

    companion object {
        private fun floor(value: Float): Float {
            return kotlin.math.floor(value)
        }
    }
}
