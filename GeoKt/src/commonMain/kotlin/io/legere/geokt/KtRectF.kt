@file:Suppress("unused", "TooManyFunctions")

package io.legere.geokt

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Interface defining common operations for float rectangles.
 * Shared between Immutable and Mutable versions to avoid code duplication.
 */
interface FloatRectValues {
    val left: Float
    val top: Float
    val right: Float
    val bottom: Float
}

val FloatRectValues.width get() = right - left
val FloatRectValues.height get() = bottom - top
val FloatRectValues.centerX get() = (left + right) / 2f
val FloatRectValues.centerY get() = (top + bottom) / 2f
val FloatRectValues.isEmpty get() = left >= right || top >= bottom

abstract class BaseRectF :
    FloatRectValues,
    RectInterface<Float, KtImmutableRectF, FloatRectValues> {
    override fun height(): Float = height

    override fun width(): Float = width

    override fun centerX(): Float = centerX

    override fun centerY(): Float = centerY

    override fun intersects(other: FloatRectValues): Boolean {
        if (this.isEmpty || other.isEmpty) return false
        return !(right < other.left || left > other.right || bottom < other.top || top > other.bottom)
    }

    override fun intersects(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): Boolean {
        if (this.isEmpty) return false
        return !(this.right < left || this.left > right || this.bottom < top || this.top > bottom)
    }

    override fun contains(
        x: Float,
        y: Float,
    ): Boolean {
        if (this.isEmpty) return false
        return (x in left..<right && y >= top && y < bottom)
    }

    override fun contains(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): Boolean {
        if (this.isEmpty) return false
        return this.left <= left &&
                this.top <= top &&
                this.right >= right &&
                this.bottom >= bottom
    }

    override fun contains(rect: FloatRectValues): Boolean {
        if (this.isEmpty) return false
        return !rect.isEmpty &&
                left <= rect.left &&
                top <= rect.top &&
                right >= rect.right &&
                bottom >= rect.bottom
    }

    fun contains(other: IntRectValues): Boolean {
        if (this.isEmpty) return false
        return !other.isEmpty &&
                left <= other.left &&
                top <= other.top &&
                right >= other.right &&
                bottom >= other.bottom
    }

    fun round(dst: KtRect) {
        dst.left = left.roundToInt()
        dst.top = top.roundToInt()
        dst.right = right.roundToInt()
        dst.bottom = bottom.roundToInt()
    }

    fun roundOut(dst: KtRect) {
        dst.left = floor(this.left).toInt()
        dst.top = floor(this.top).toInt()
        dst.right = ceil(this.right).toInt()
        dst.bottom = ceil(this.bottom).toInt()
    }

    override fun isEmpty(): Boolean = isEmpty
}

data class KtImmutableRectF(
    override val left: Float,
    override val top: Float,
    override val right: Float,
    override val bottom: Float,
) : BaseRectF(),
    ImmutableRectInterface<Float, KtImmutableRectF, FloatRectValues> {
    constructor() : this(0f, 0f, 0f, 0f)
    constructor(rectF: FloatRectValues) : this(rectF.left, rectF.top, rectF.right, rectF.bottom)
    constructor(rectF: IntRectValues) : this(rectF.left.toFloat(), rectF.top.toFloat(), rectF.right.toFloat(), rectF.bottom.toFloat())

    override fun inset(
        dx: Float,
        dy: Float,
    ): KtImmutableRectF {
        if (dx == 0f && dy == 0f) return this
        return KtImmutableRectF(
            left = left + dx,
            top = top + dy,
            right = right - dx,
            bottom = bottom - dy,
        )
    }

    override fun inset(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): KtImmutableRectF =
        KtImmutableRectF(
            left = this.left + left,
            top = this.top + top,
            right = this.right - right,
            bottom = this.bottom - right,
        )

    override fun intersect(other: FloatRectValues): KtImmutableRectF {
        if (this.isEmpty || other.isEmpty) return this
        return KtImmutableRectF(
            left = max(left, other.left),
            top = max(top, other.top),
            right = min(right, other.right),
            bottom = min(bottom, other.bottom),
        )
    }

    override fun intersect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): KtImmutableRectF {
        if (this.isEmpty) return this
        return KtImmutableRectF(
            left = max(left, left),
            top = max(top, top),
            right = min(right, right),
            bottom = min(bottom, bottom),
        )
    }

    fun setEmpty(): KtImmutableRectF =
        KtImmutableRectF(
            left = 0f,
            top = 0f,
            right = 0f,
            bottom = 0f,
        )

    override fun union(other: FloatRectValues): KtImmutableRectF =
        when {
            other.isEmpty -> {
                this
            }

            this.isEmpty -> {
                other as? KtImmutableRectF ?: KtImmutableRectF(other.left, other.top, other.right, other.bottom)
            }

            else -> {
                KtImmutableRectF(
                    left = min(left, other.left),
                    top = min(top, other.top),
                    right = max(right, other.right),
                    bottom = max(bottom, other.bottom),
                )
            }
        }

    override fun union(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ): KtImmutableRectF =
        when {
            this.isEmpty -> {
                KtImmutableRectF(left, top, right, bottom)
            }

            else -> {
                KtImmutableRectF(
                    left = min(left, this.left),
                    top = min(top, this.top),
                    right = max(right, this.right),
                    bottom = max(bottom, this.bottom),
                )
            }
        }

    override fun union(
        x: Float,
        y: Float,
    ): KtImmutableRectF =
        when {
            this.isEmpty -> {
                KtImmutableRectF()
            }

            else -> {
                KtImmutableRectF(
                    left = min(x, this.left),
                    top = min(y, this.top),
                    right = max(x, this.right),
                    bottom = max(y, this.bottom),
                )
            }
        }

    fun roundOut(): KtImmutableRect =
        KtImmutableRect(
            left = floor(this.left).toInt(),
            top = floor(this.top).toInt(),
            right = ceil(this.right).toInt(),
            bottom = ceil(this.bottom).toInt(),
        )

    override fun offset(
        dx: Float,
        dy: Float,
    ): KtImmutableRectF =
        KtImmutableRectF(
            left + dx,
            top + dy,
            right + dx,
            bottom + dy,
        )

    override fun offsetTo(
        newLeft: Float,
        newTop: Float,
    ): KtImmutableRectF {
        val width = right - left
        val height = bottom - top
        return KtImmutableRectF(
            newLeft,
            newTop,
            newLeft + width,
            newTop + height,
        )
    }

    override fun sort(): KtImmutableRectF =
        KtImmutableRectF(
            left = min(left, right),
            top = min(top, bottom),
            right = max(left, right),
            bottom = max(top, bottom),
        )

    fun toMutable() = KtRectF(left, top, right, bottom)

    fun toFloatArray(): FloatArray = floatArrayOf(left, top, right, bottom)

    companion object {
        val EMPTY = KtImmutableRectF(0f, 0f, 0f, 0f)
    }
}

data class KtRectF(
    override var left: Float = 0f,
    override var top: Float = 0f,
    override var right: Float = 0f,
    override var bottom: Float = 0f,
) : BaseRectF(),
    MutableRectInterface<Float, KtImmutableRectF, FloatRectValues> {
    constructor() : this(0f, 0f, 0f, 0f)
    constructor(rectF: FloatRectValues) : this(rectF.left, rectF.top, rectF.right, rectF.bottom)
    constructor(rectF: IntRectValues) : this(
        rectF.left.toFloat(),
        rectF.top.toFloat(),
        rectF.right.toFloat(),
        rectF.bottom.toFloat(),
    )

    fun set(
        l: Float,
        t: Float,
        r: Float,
        b: Float,
    ) {
        left = l
        top = t
        right = r
        bottom = b
    }

    fun set(src: FloatRectValues) {
        set(src.left, src.top, src.right, src.bottom)
    }

    fun set(src: IntRectValues) {
        set(src.left.toFloat(), src.top.toFloat(), src.right.toFloat(), src.bottom.toFloat())
    }

    fun setEmpty() {
        left = 0f
        top = 0f
        right = 0f
        bottom = 0f
    }

    override fun inset(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ) {
        this.left += left
        this.top += top
        this.right -= right
        this.bottom -= right
    }

    override fun inset(
        dx: Float,
        dy: Float,
    ) {
        if (dx == 0f && dy == 0f) return
        left += dx
        top += dy
        right -= dx
        bottom -= dy
    }

    override fun intersect(other: FloatRectValues) {
        if (this.isEmpty || other.isEmpty) return
        left = max(left, other.left)
        top = max(top, other.top)
        right = min(right, other.right)
        bottom = min(bottom, other.bottom)
    }

    override fun intersect(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ) {
        if (this.isEmpty) return
        this.left = max(this.left, left)
        this.top = max(this.top, top)
        this.right = min(this.right, right)
        this.bottom = min(this.bottom, bottom)
    }

    override fun union(other: FloatRectValues) {
        when
        {
            other.isEmpty -> {
            }

            this.isEmpty -> {
                left = other.left
                top = other.top
                right = other.right
                bottom = other.bottom
            }

            else -> {
                left = min(left, other.left)
                top = min(top, other.top)
                right = max(right, other.right)
                bottom = max(bottom, other.bottom)
            }
        }
    }

    override fun union(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ) {
        when {
            this.isEmpty -> {
                this.left = left
                this.top = top
                this.right = right
                this.bottom = bottom
            }

            else -> {
                this.left = min(left, this.left)
                this.top = min(top, this.top)
                this.right = max(right, this.right)
                this.bottom = max(bottom, this.bottom)
            }
        }
    }

    override fun union(
        x: Float,
        y: Float,
    ) {
        when {
            this.isEmpty -> {
                return
            }

            else -> {
                this.left = min(x, this.left)
                this.top = min(y, this.top)
                this.right = max(x, this.right)
                this.bottom = max(y, this.bottom)
            }
        }
    }

    fun roundOut(): KtRect =
        KtRect(
            left = floor(this.left).toInt(),
            top = floor(this.top).toInt(),
            right = ceil(this.right).toInt(),
            bottom = ceil(this.bottom).toInt(),
        )

    override fun intersects(other: FloatRectValues): Boolean {
        if (this.isEmpty || other.isEmpty) return false
        return !(right < other.left || left > other.right || bottom < other.top || top > other.bottom)
    }

    override fun offset(
        dx: Float,
        dy: Float,
    ) {
        left += dx
        top += dy
        right += dx
        bottom += dy
    }

    override fun offsetTo(
        newLeft: Float,
        newTop: Float,
    ) {
        val width = right - left
        val height = bottom - top
        left = newLeft
        top = newTop
        right = newLeft + width
        bottom = newTop + height
    }

    override fun sort() {
        val newLeft = min(left, right)
        val newTop = min(top, bottom)
        val newRight = max(left, right)
        val newBottom = max(top, bottom)
        left = newLeft
        top = newTop
        right = newRight
        bottom = newBottom
    }

    fun toImmutable() = KtImmutableRectF(left, top, right, bottom)

    fun toFloatArray(): FloatArray = floatArrayOf(left, top, right, bottom)

    companion object {
        val EMPTY = KtImmutableRectF(0f, 0f, 0f, 0f)
    }
}
