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

interface FloatPointValues {
    val x: Float
    val y: Float
}

data class KtImmutablePointF(
    override val x: Float,
    override val y: Float,
) : FloatPointValues,
    ImmutablePointInterface<Float, KtImmutablePointF> {
    fun toFloatArray(): FloatArray = floatArrayOf(x, y)

    fun toMutable() = KtPointF(x, y)

    override fun offset(
        dx: Float,
        dy: Float,
    ) = KtImmutablePointF(x + dx, y + dy)

    override fun negate() = KtImmutablePointF(-x, -y)

    override fun length() = kotlin.math.sqrt(x * x + y * y)

    companion object {
        val ZERO = KtImmutablePointF(0f, 0f)
    }
}

data class KtPointF(
    override var x: Float = 0f,
    override var y: Float = 0f,
) : FloatPointValues,
    MutablePointInterface<Float, KtImmutablePointF> {
    fun toFloatArray(): FloatArray = floatArrayOf(x, y)

    fun set(
        x: Float,
        y: Float,
    ) {
        this.x = x
        this.y = y
    }

    fun set(src: FloatPointValues) {
        this.x = src.x
        this.y = src.y
    }

    fun toImmutable() = KtImmutablePointF(x, y)

    override fun offset(
        dx: Float,
        dy: Float,
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
