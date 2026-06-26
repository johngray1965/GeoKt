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

/**
 * The double sibling of [FloatPointValues]: use it for the central coordinate model so values
 * aren't lossily widened from/narrowed to Float; convert to Float only at a render/layout boundary.
 */
interface DoublePointValues {
    val x: Double
    val y: Double
}

data class KtImmutablePointD(
    override val x: Double,
    override val y: Double,
) : DoublePointValues,
    ImmutablePointInterface<Double, KtImmutablePointD> {
    constructor() : this(0.0, 0.0)
    constructor(point: DoublePointValues) : this(point.x, point.y)
    constructor(point: FloatPointValues) : this(point.x.toDouble(), point.y.toDouble())

    fun toDoubleArray(): DoubleArray = doubleArrayOf(x, y)

    fun toFloatArray(): FloatArray = floatArrayOf(x.toFloat(), y.toFloat())

    /** Narrow to a Float point — for a render/layout boundary only (the central model stays double). */
    fun toFloat() = KtImmutablePointF(x.toFloat(), y.toFloat())

    fun toMutable() = KtPointD(x, y)

    override fun offset(
        dx: Double,
        dy: Double,
    ) = KtImmutablePointD(x + dx, y + dy)

    override fun negate() = KtImmutablePointD(-x, -y)

    override fun length() = kotlin.math.sqrt(x * x + y * y)

    companion object {
        val ZERO = KtImmutablePointD(0.0, 0.0)
    }
}

data class KtPointD(
    override var x: Double = 0.0,
    override var y: Double = 0.0,
) : DoublePointValues,
    MutablePointInterface<Double, KtImmutablePointD> {
    constructor() : this(0.0, 0.0)
    constructor(point: DoublePointValues) : this(point.x, point.y)
    constructor(point: FloatPointValues) : this(point.x.toDouble(), point.y.toDouble())

    fun toDoubleArray(): DoubleArray = doubleArrayOf(x, y)

    fun toFloatArray(): FloatArray = floatArrayOf(x.toFloat(), y.toFloat())

    /** Narrow to a Float point — for a render/layout boundary only (the central model stays double). */
    fun toFloat() = KtImmutablePointF(x.toFloat(), y.toFloat())

    fun set(
        x: Double,
        y: Double,
    ) {
        this.x = x
        this.y = y
    }

    fun set(src: DoublePointValues) {
        this.x = src.x
        this.y = src.y
    }

    fun set(src: FloatPointValues) {
        this.x = src.x.toDouble()
        this.y = src.y.toDouble()
    }

    fun toImmutable() = KtImmutablePointD(x, y)

    override fun offset(
        dx: Double,
        dy: Double,
    ) {
        x += dx
        y += dy
    }

    override fun negate() {
        x = -x
        y = -y
    }

    override fun length() = kotlin.math.sqrt(x * x + y * y)
}
