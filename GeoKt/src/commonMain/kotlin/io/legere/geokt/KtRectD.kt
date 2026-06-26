/*
 * Copyright (c) 2026 Legere Technologies LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Some of the mathematical logic in this file is a direct translation
 * of the C++ implementation in Google's Skia Graphics Library, which
 * is also licensed under a permissive open-source license.
 *
 */

@file:Suppress("unused", "TooManyFunctions")

package io.legere.geokt

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Interface defining common operations for double rectangles.
 * Shared between Immutable and Mutable versions to avoid code duplication.
 *
 * The double sibling of [FloatRectValues]: use it for the central coordinate model so values
 * aren't lossily widened from/narrowed to Float; convert to Float only at a render/layout boundary.
 */
interface DoubleRectValues {
    val left: Double
    val top: Double
    val right: Double
    val bottom: Double
}

val DoubleRectValues.width get() = right - left
val DoubleRectValues.height get() = bottom - top
val DoubleRectValues.centerX get() = (left + right) / 2.0
val DoubleRectValues.centerY get() = (top + bottom) / 2.0
val DoubleRectValues.isEmpty get() = left >= right || top >= bottom

abstract class BaseRectD :
    DoubleRectValues,
    RectInterface<Double, KtImmutableRectD, DoubleRectValues> {
    override fun height(): Double = height

    override fun width(): Double = width

    override fun centerX(): Double = centerX

    override fun centerY(): Double = centerY

    override fun intersects(other: DoubleRectValues): Boolean {
        if (this.isEmpty || other.isEmpty) return false
        return !(right < other.left || left > other.right || bottom < other.top || top > other.bottom)
    }

    override fun intersects(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ): Boolean {
        if (this.isEmpty) return false
        return !(this.right < left || this.left > right || this.bottom < top || this.top > bottom)
    }

    override fun contains(
        x: Double,
        y: Double,
    ): Boolean {
        if (this.isEmpty) return false
        return (x in left..<right && y >= top && y < bottom)
    }

    override fun contains(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ): Boolean {
        if (this.isEmpty) return false
        return this.left <= left &&
                this.top <= top &&
                this.right >= right &&
                this.bottom >= bottom
    }

    override fun contains(rect: DoubleRectValues): Boolean {
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

data class KtImmutableRectD(
    override val left: Double,
    override val top: Double,
    override val right: Double,
    override val bottom: Double,
) : BaseRectD(),
    ImmutableRectInterface<Double, KtImmutableRectD, DoubleRectValues> {
    constructor() : this(0.0, 0.0, 0.0, 0.0)
    constructor(rect: DoubleRectValues) : this(rect.left, rect.top, rect.right, rect.bottom)
    constructor(rect: FloatRectValues) : this(
        rect.left.toDouble(),
        rect.top.toDouble(),
        rect.right.toDouble(),
        rect.bottom.toDouble(),
    )
    constructor(rect: IntRectValues) : this(
        rect.left.toDouble(),
        rect.top.toDouble(),
        rect.right.toDouble(),
        rect.bottom.toDouble(),
    )

    override fun inset(
        dx: Double,
        dy: Double,
    ): KtImmutableRectD {
        if (dx == 0.0 && dy == 0.0) return this
        return KtImmutableRectD(
            left = left + dx,
            top = top + dy,
            right = right - dx,
            bottom = bottom - dy,
        )
    }

    override fun inset(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ): KtImmutableRectD =
        KtImmutableRectD(
            left = this.left + left,
            top = this.top + top,
            right = this.right - right,
            bottom = this.bottom - bottom,
        )

    override fun intersect(other: DoubleRectValues): KtImmutableRectD {
        if (this.isEmpty || other.isEmpty) return this
        return KtImmutableRectD(
            left = max(left, other.left),
            top = max(top, other.top),
            right = min(right, other.right),
            bottom = min(bottom, other.bottom),
        )
    }

    override fun intersect(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ): KtImmutableRectD {
        if (this.isEmpty) return this
        return KtImmutableRectD(
            left = max(this.left, left),
            top = max(this.top, top),
            right = min(this.right, right),
            bottom = min(this.bottom, bottom),
        )
    }

    fun setEmpty(): KtImmutableRectD = EMPTY

    override fun union(other: DoubleRectValues): KtImmutableRectD =
        when {
            other.isEmpty -> {
                this
            }

            this.isEmpty -> {
                other as? KtImmutableRectD ?: KtImmutableRectD(other.left, other.top, other.right, other.bottom)
            }

            else -> {
                KtImmutableRectD(
                    left = min(left, other.left),
                    top = min(top, other.top),
                    right = max(right, other.right),
                    bottom = max(bottom, other.bottom),
                )
            }
        }

    override fun union(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ): KtImmutableRectD =
        when {
            this.isEmpty -> {
                KtImmutableRectD(left, top, right, bottom)
            }

            else -> {
                KtImmutableRectD(
                    left = min(left, this.left),
                    top = min(top, this.top),
                    right = max(right, this.right),
                    bottom = max(bottom, this.bottom),
                )
            }
        }

    override fun union(
        x: Double,
        y: Double,
    ): KtImmutableRectD =
        when {
            this.isEmpty -> {
                KtImmutableRectD()
            }

            else -> {
                KtImmutableRectD(
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
        dx: Double,
        dy: Double,
    ): KtImmutableRectD =
        KtImmutableRectD(
            left + dx,
            top + dy,
            right + dx,
            bottom + dy,
        )

    override fun offsetTo(
        newLeft: Double,
        newTop: Double,
    ): KtImmutableRectD {
        val width = right - left
        val height = bottom - top
        return KtImmutableRectD(
            newLeft,
            newTop,
            newLeft + width,
            newTop + height,
        )
    }

    override fun sort(): KtImmutableRectD =
        KtImmutableRectD(
            left = min(left, right),
            top = min(top, bottom),
            right = max(left, right),
            bottom = max(top, bottom),
        )

    fun toMutable() = KtRectD(left, top, right, bottom)

    /** Narrow to a Float rect — for a render/layout boundary only (the central model stays double). */
    fun toFloat() = KtImmutableRectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

    fun toDoubleArray(): DoubleArray = doubleArrayOf(left, top, right, bottom)

    fun toFloatArray(): FloatArray = floatArrayOf(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

    companion object {
        val EMPTY = KtImmutableRectD(0.0, 0.0, 0.0, 0.0)
    }
}

data class KtRectD(
    override var left: Double = 0.0,
    override var top: Double = 0.0,
    override var right: Double = 0.0,
    override var bottom: Double = 0.0,
) : BaseRectD(),
    MutableRectInterface<Double, KtImmutableRectD, DoubleRectValues> {
    constructor() : this(0.0, 0.0, 0.0, 0.0)
    constructor(rect: DoubleRectValues) : this(rect.left, rect.top, rect.right, rect.bottom)
    constructor(rect: FloatRectValues) : this(
        rect.left.toDouble(),
        rect.top.toDouble(),
        rect.right.toDouble(),
        rect.bottom.toDouble(),
    )
    constructor(rect: IntRectValues) : this(
        rect.left.toDouble(),
        rect.top.toDouble(),
        rect.right.toDouble(),
        rect.bottom.toDouble(),
    )

    fun set(
        l: Double,
        t: Double,
        r: Double,
        b: Double,
    ) {
        left = l
        top = t
        right = r
        bottom = b
    }

    fun set(src: DoubleRectValues) {
        set(src.left, src.top, src.right, src.bottom)
    }

    fun set(src: FloatRectValues) {
        set(src.left.toDouble(), src.top.toDouble(), src.right.toDouble(), src.bottom.toDouble())
    }

    fun set(src: IntRectValues) {
        set(src.left.toDouble(), src.top.toDouble(), src.right.toDouble(), src.bottom.toDouble())
    }

    fun setEmpty() {
        left = 0.0
        top = 0.0
        right = 0.0
        bottom = 0.0
    }

    override fun inset(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ) {
        this.left += left
        this.top += top
        this.right -= right
        this.bottom -= bottom
    }

    override fun inset(
        dx: Double,
        dy: Double,
    ) {
        if (dx == 0.0 && dy == 0.0) return
        left += dx
        top += dy
        right -= dx
        bottom -= dy
    }

    override fun intersect(other: DoubleRectValues) {
        if (this.isEmpty || other.isEmpty) return
        left = max(left, other.left)
        top = max(top, other.top)
        right = min(right, other.right)
        bottom = min(bottom, other.bottom)
    }

    override fun intersect(
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
    ) {
        if (this.isEmpty) return
        this.left = max(this.left, left)
        this.top = max(this.top, top)
        this.right = min(this.right, right)
        this.bottom = min(this.bottom, bottom)
    }

    override fun union(other: DoubleRectValues) {
        when {
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
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
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
        x: Double,
        y: Double,
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

    override fun intersects(other: DoubleRectValues): Boolean {
        if (this.isEmpty || other.isEmpty) return false
        return !(right < other.left || left > other.right || bottom < other.top || top > other.bottom)
    }

    override fun offset(
        dx: Double,
        dy: Double,
    ) {
        left += dx
        top += dy
        right += dx
        bottom += dy
    }

    override fun offsetTo(
        newLeft: Double,
        newTop: Double,
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

    fun toImmutable() = KtImmutableRectD(left, top, right, bottom)

    /** Narrow to a Float rect — for a render/layout boundary only (the central model stays double). */
    fun toFloat() = KtImmutableRectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

    fun toDoubleArray(): DoubleArray = doubleArrayOf(left, top, right, bottom)

    fun toFloatArray(): FloatArray = floatArrayOf(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

    companion object {
        val EMPTY = KtImmutableRectD(0.0, 0.0, 0.0, 0.0)
    }
}
