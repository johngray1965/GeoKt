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

package io.legere.geokt

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val DEGREES_TO_RADIANS = (PI / 180.0)

internal fun DoubleArray.setRotate(
    degrees: Float,
    px: Float,
    py: Float,
) {
    setRotate(degrees.toDouble(), px.toDouble(), py.toDouble())
}

@Suppress("MagicNumber")
internal fun DoubleArray.setRotate(
    degrees: Double,
    px: Double,
    py: Double,
) {
    // Note we do the same chunk of math for the cos/sin in 6 places.
    // It would be nice to have one common function, right?
    // DO NOT DO IT.  This is fast, very fast.  You can't make
    // this cleaner without making it slower (I've spent a lot of time trying).
    val normalizedDegrees = (degrees % 360.0 + 360.0) % 360.0

    val sin: Double
    val cos: Double
    when (degrees) {
        0.0 -> { // Exact 0 degrees
            sin = 0.0
            cos = 1.0
        }

        90.0 -> { // Exact 90 degrees
            sin = 1.0
            cos = 0.0
        }

        180.0 -> {
            sin = 0.0
            cos = -1.0
        }

        270.0 -> {
            sin = -1.0
            cos = 0.0
        }

        else -> {
            val radians = normalizedDegrees * DEGREES_TO_RADIANS
            val sina = sin(radians)
            val cosa = cos(radians)
            sin = if (fastAbs(sina) < ZERO_TOLERANCE) 0.0 else sina
            cos = if (fastAbs(cosa) < ZERO_TOLERANCE) 0.0 else cosa
        }
    }

    this[SCALE_X] = cos
    this[SKEW_X] = -sin
    this[SKEW_Y] = sin
    this[SCALE_Y] = cos
    this[TRANS_X] = (px - cos * px + sin * py)
    this[TRANS_Y] = (py - sin * px - cos * py)
    this[PERSP_0] = 0.0
    this[PERSP_1] = 0.0
    this[PERSP_2] = 1.0
}

@Suppress("MagicNumber")
internal fun DoubleArray.setRotate(degrees: Float) {
    setRotate(degrees.toDouble())
}

@Suppress("MagicNumber")
internal fun DoubleArray.setRotate(degrees: Double) {
    // Note we do the same chunk of math for the cos/sin in 6 places.
    // It would be nice to have one common function, right?
    // DO NOT DO IT.  This is fast, very fast.  You can't make
    // this cleaner without making it slower (I've spent a lot of time trying).
    val normalizedDegrees = (degrees % 360.0 + 360.0) % 360.0

    val sin: Double
    val cos: Double
    when (degrees) {
        0.0 -> { // Exact 0 degrees
            sin = 0.0
            cos = 1.0
        }

        90.0 -> { // Exact 90 degrees
            sin = 1.0
            cos = 0.0
        }

        180.0 -> {
            sin = 0.0
            cos = -1.0
        }

        270.0 -> {
            sin = -1.0
            cos = 0.0
        }

        else -> {
            val radians = normalizedDegrees * DEGREES_TO_RADIANS
            val sina = sin(radians)
            val cosa = cos(radians)
            sin = if (fastAbs(sina) < ZERO_TOLERANCE) 0.0 else sina
            cos = if (fastAbs(cosa) < ZERO_TOLERANCE) 0.0 else cosa
        }
    }

    this[SCALE_X] = cos
    this[SKEW_X] = -sin
    this[SKEW_Y] = sin
    this[SCALE_Y] = cos
    this[TRANS_X] = 0.0
    this[TRANS_Y] = 0.0
    this[PERSP_0] = 0.0
    this[PERSP_1] = 0.0
    this[PERSP_2] = 1.0
}

internal fun DoubleArray.postRotate(degrees: Float) {
    postRotate(degrees.toDouble())
}

@Suppress("MagicNumber")
internal fun DoubleArray.postRotate(degrees: Double) {
    // Note we do the same chunk of math for the cos/sin in 6 places.
    // It would be nice to have one common function, right?
    // DO NOT DO IT.  This is fast, very fast.  You can't make
    // this cleaner without making it slower (I've spent a lot of time trying).
    val normalizedDegrees = (degrees % 360.0 + 360.0) % 360.0

    val sin: Double
    val cos: Double
    when (degrees) {
        0.0 -> { // Exact 0 degrees
            sin = 0.0
            cos = 1.0
        }

        90.0 -> { // Exact 90 degrees
            sin = 1.0
            cos = 0.0
        }

        180.0 -> {
            sin = 0.0
            cos = -1.0
        }

        270.0 -> {
            sin = -1.0
            cos = 0.0
        }

        else -> {
            val radians = normalizedDegrees * DEGREES_TO_RADIANS
            val sina = sin(radians)
            val cosa = cos(radians)
            sin = if (fastAbs(sina) < ZERO_TOLERANCE) 0.0 else sina
            cos = if (fastAbs(cosa) < ZERO_TOLERANCE) 0.0 else cosa
        }
    }

    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]

    this[SCALE_X] = cos * j - sin * m
    this[SKEW_X] = cos * k - sin * n
    this[TRANS_X] = cos * l - sin * o
    this[SKEW_Y] = sin * j + cos * m
    this[SCALE_Y] = sin * k + cos * n
    this[TRANS_Y] = sin * l + cos * o
}

/**
 *         Given:
 *
 *                      | J K L |                        | c -s dx |
 *             Matrix = | M N O |,  R(degrees, px, py) = | s  c dy |
 *                      | P Q R |                        | 0  0  1 |
 *
 *         where
 *
 *             c  = cos(degrees)
 *             s  = sin(degrees)
 *             dx =  s * py + (1 - c) * px
 *             dy = -s * px + (1 - c) * py
 *
 *         sets SkMatrix to:
 *
 *                                           |c -s dx| |J K L|   |cJ-sM+dx*P cK-sN+dx*Q cL-sO+dx+R|
 *             R(degrees, px, py) * Matrix = |s  c dy| |M N O| = |sJ+cM+dy*P sK+cN+dy*Q sL+cO+dy*R|
 *                                           |0  0  1| |P Q R|   |         P          Q          R|
 *
 */
internal fun DoubleArray.postRotate(
    degrees: Float,
    px: Float,
    py: Float,
) {
    postRotate(degrees.toDouble(), px.toDouble(), py.toDouble())
}

@Suppress("MagicNumber")
internal fun DoubleArray.postRotate(
    degrees: Double,
    px: Double,
    py: Double,
) {
    // Note we do the same chunk of math for the cos/sin in 6 places.
    // It would be nice to have one common function, right?
    // DO NOT DO IT.  This is fast, very fast.  You can't make
    // this cleaner without making it slower (I've spent a lot of time trying).
    val normalizedDegrees = (degrees % 360.0 + 360.0) % 360.0

    val sin: Double
    val cos: Double
    when (degrees) {
        0.0 -> { // Exact 0 degrees
            sin = 0.0
            cos = 1.0
        }

        90.0 -> { // Exact 90 degrees
            sin = 1.0
            cos = 0.0
        }

        180.0 -> {
            sin = 0.0
            cos = -1.0
        }

        270.0 -> {
            sin = -1.0
            cos = 0.0
        }

        else -> {
            val radians = normalizedDegrees * DEGREES_TO_RADIANS
            val sina = sin(radians)
            val cosa = cos(radians)
            sin = if (fastAbs(sina) < ZERO_TOLERANCE) 0.0 else sina
            cos = if (fastAbs(cosa) < ZERO_TOLERANCE) 0.0 else cosa
        }
    }

    val dx = sin * py + (1 - cos) * px
    val dy = -sin * px + (1 - cos) * py

    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]
    val p = this[PERSP_0]
    val q = this[PERSP_1]
    val r = this[PERSP_2]

    this[SCALE_X] = cos * j - sin * m + dx * p
    this[SKEW_X] = cos * k - sin * n + dx * q
    this[TRANS_X] = cos * l - sin * o + dx * r
    this[SKEW_Y] = sin * j + cos * m + dy * p
    this[SCALE_Y] = sin * k + cos * n + dy * q
    this[TRANS_Y] = sin * l + cos * o + dy * r
}

/**
 *         Given:
 *
 *                      | A B C |                        | c -s dx |
 *             Matrix = | D E F |,  R(degrees, px, py) = | s  c dy |
 *                      | G H I |                        | 0  0  1 |
 *
 *         where
 *
 *             c  = cos(degrees)
 *             s  = sin(degrees)
 *             dx =  s * py + (1 - c) * px
 *             dy = -s * px + (1 - c) * py
 *
 *         sets SkMatrix to:
 *
 *                                           | A B C | | c -s dx |   | Ac+Bs -As+Bc A*dx+B*dy+C |
 *             Matrix * R(degrees, px, py) = | D E F | | s  c dy | = | Dc+Es -Ds+Ec D*dx+E*dy+F |
 *                                           | G H I | | 0  0  1 |   | Gc+Hs -Gs+Hc G*dx+H*dy+I |
 *
 *
 */
internal fun DoubleArray.preRotate(
    degrees: Float,
    px: Float,
    py: Float,
) {
    preRotate(degrees.toDouble(), px.toDouble(), py.toDouble())
}

@Suppress("MagicNumber")
internal fun DoubleArray.preRotate(
    degrees: Double,
    px: Double,
    py: Double,
) {
    // Note we do the same chunk of math for the cos/sin in 6 places.
    // It would be nice to have one common function, right?
    // DO NOT DO IT.  This is fast, very fast.  You can't make
    // this cleaner without making it slower (I've spent a lot of time trying).
    val normalizedDegrees = (degrees % 360.0 + 360.0) % 360.0

    val sin: Double
    val cos: Double
    when (degrees) {
        0.0 -> { // Exact 0 degrees
            sin = 0.0
            cos = 1.0
        }

        90.0 -> { // Exact 90 degrees
            sin = 1.0
            cos = 0.0
        }

        180.0 -> {
            sin = 0.0
            cos = -1.0
        }

        270.0 -> {
            sin = -1.0
            cos = 0.0
        }

        else -> {
            val radians = normalizedDegrees * DEGREES_TO_RADIANS
            val sina = sin(radians)
            val cosa = cos(radians)
            sin = if (fastAbs(sina) < ZERO_TOLERANCE) 0.0 else sina
            cos = if (fastAbs(cosa) < ZERO_TOLERANCE) 0.0 else cosa
        }
    }

    val dx = sin * py + (1 - cos) * px
    val dy = -sin * px + (1 - cos) * py

    val a = this[SCALE_X]
    val b = this[SKEW_X]
    val c = this[TRANS_X]
    val d = this[SKEW_Y]
    val e = this[SCALE_Y]
    val f = this[TRANS_Y]
    val g = this[PERSP_0]
    val h = this[PERSP_1]
    val i = this[PERSP_2]

    this[SCALE_X] = a * cos + b * sin
    this[SKEW_X] = -a * sin + b * cos
    this[TRANS_X] = a * dx + b * dy + c
    this[SKEW_Y] = (d * cos + e * sin)
    this[SCALE_Y] = (d * -sin + e * cos)
    this[TRANS_Y] = d * dx + e * dy + f
    this[PERSP_0] = g * cos + h * sin
    this[PERSP_1] = -g * sin + h * cos
    this[PERSP_2] = g * dx + h * dy + i
}

internal fun DoubleArray.preRotate(degrees: Float) {
    preRotate(degrees.toDouble())
}

@Suppress("MagicNumber")
internal fun DoubleArray.preRotate(degrees: Double) {
    // Note we do the same chunk of math for the cos/sin in 6 places.
    // It would be nice to have one common function, right?
    // DO NOT DO IT.  This is fast, very fast.  You can't make
    // this cleaner without making it slower (I've spent a lot of time trying).
    val normalizedDegrees = (degrees % 360.0 + 360.0) % 360.0

    val sin: Double
    val cos: Double
    when (degrees) {
        0.0 -> { // Exact 0 degrees
            sin = 0.0
            cos = 1.0
        }

        90.0 -> { // Exact 90 degrees
            sin = 1.0
            cos = 0.0
        }

        180.0 -> {
            sin = 0.0
            cos = -1.0
        }

        270.0 -> {
            sin = -1.0
            cos = 0.0
        }

        else -> {
            val radians = normalizedDegrees * DEGREES_TO_RADIANS
            val sina = sin(radians)
            val cosa = cos(radians)
            sin = if (fastAbs(sina) < ZERO_TOLERANCE) 0.0 else sina
            cos = if (fastAbs(cosa) < ZERO_TOLERANCE) 0.0 else cosa
        }
    }

    val a = this[SCALE_X]
    val b = this[SKEW_X]
    val c = this[TRANS_X]
    val d = this[SKEW_Y]
    val e = this[SCALE_Y]
    val f = this[TRANS_Y]
    val g = this[PERSP_0]
    val h = this[PERSP_1]
    val i = this[PERSP_2]

    this[SCALE_X] = a * cos + b * sin
    this[SKEW_X] = -a * sin + b * cos
    this[TRANS_X] = c
    this[SKEW_Y] = (d * cos + e * sin)
    this[SCALE_Y] = (d * -sin + e * cos)
    this[TRANS_Y] = f
    this[PERSP_0] = g * cos + h * sin
    this[PERSP_1] = -g * sin + h * cos
    this[PERSP_2] = i
}
