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

const val ZERO_TOLERANCE = 1.0 / (1 shl 16)

// Custom abs function for Double to avoid potential overhead of kotlin.math.abs
internal fun fastAbs(x: Double) = if (x < 0) -x else x

internal fun DoubleArray.reset() {
    fill(0.0)
    this[SCALE_X] = 1.0
    this[SCALE_Y] = 1.0
    this[PERSP_2] = 1.0
}

internal fun DoubleArray.isIdentity(): Boolean =
    this[SCALE_X] == 1.0 && this[SKEW_X] == 0.0 && this[TRANS_X] == 0.0 &&
        this[SKEW_Y] == 0.0 && this[SCALE_Y] == 1.0 && this[TRANS_Y] == 0.0 &&
        this[PERSP_0] == 0.0 && this[PERSP_1] == 0.0 && this[PERSP_2] == 1.0

internal fun DoubleArray.isAffine(): Boolean = this[PERSP_0] == 0.0 && this[PERSP_1] == 0.0 && this[PERSP_2] == 1.0

@Suppress("MagicNumber", "UnnecessaryVariable")
internal fun DoubleArray.preConcat(other: DoubleArray) {
    val a = this
    val b = other
    // note that a = this, so the temp variables are important.
    // without them, we would overwrite the original matrix
    // before the values are calculated
    val a0 = a[SCALE_X]
    val a1 = a[SKEW_X]
    val a2 = a[TRANS_X]
    val a3 = a[SKEW_Y]
    val a4 = a[SCALE_Y]
    val a5 = a[TRANS_Y]
    val a6 = a[PERSP_0]
    val a7 = a[PERSP_1]
    val a8 = a[PERSP_2]

    val b0 = b[SCALE_X]
    val b1 = b[SKEW_X]
    val b2 = b[TRANS_X]
    val b3 = b[SKEW_Y]
    val b4 = b[SCALE_Y]
    val b5 = b[TRANS_Y]
    val b6 = b[PERSP_0]
    val b7 = b[PERSP_1]
    val b8 = b[PERSP_2]

    val v0 = a0 * b0 + a1 * b3 + a2 * b6
    val v1 = a0 * b1 + a1 * b4 + a2 * b7
    val v2 = a0 * b2 + a1 * b5 + a2 * b8
    val v3 = a3 * b0 + a4 * b3 + a5 * b6
    val v4 = a3 * b1 + a4 * b4 + a5 * b7
    val v5 = a3 * b2 + a4 * b5 + a5 * b8
    val v6 = a6 * b0 + a7 * b3 + a8 * b6
    val v7 = a6 * b1 + a7 * b4 + a8 * b7
    val v8 = a6 * b2 + a7 * b5 + a8 * b8
    this[SCALE_X] = v0
    this[SKEW_X] = v1
    this[TRANS_X] = v2
    this[SKEW_Y] = v3
    this[SCALE_Y] = v4
    this[TRANS_Y] = v5
    this[PERSP_0] = v6
    this[PERSP_1] = v7
    this[PERSP_2] = v8
}

@Suppress("MagicNumber", "UnnecessaryVariable")
internal fun DoubleArray.postConcat(other: DoubleArray) {
    val a = other
    val b = this
    // note that b = this, so the temp variables are important.
    // without them we would overwrite the original matrix
    // before the values are calculated
    val a0 = a[SCALE_X]
    val a1 = a[SKEW_X]
    val a2 = a[TRANS_X]
    val a3 = a[SKEW_Y]
    val a4 = a[SCALE_Y]
    val a5 = a[TRANS_Y]
    val a6 = a[PERSP_0]
    val a7 = a[PERSP_1]
    val a8 = a[PERSP_2]

    val b0 = b[SCALE_X]
    val b1 = b[SKEW_X]
    val b2 = b[TRANS_X]
    val b3 = b[SKEW_Y]
    val b4 = b[SCALE_Y]
    val b5 = b[TRANS_Y]
    val b6 = b[PERSP_0]
    val b7 = b[PERSP_1]
    val b8 = b[PERSP_2]

    val v0 = a0 * b0 + a1 * b3 + a2 * b6
    val v1 = a0 * b1 + a1 * b4 + a2 * b7
    val v2 = a0 * b2 + a1 * b5 + a2 * b8
    val v3 = a3 * b0 + a4 * b3 + a5 * b6
    val v4 = a3 * b1 + a4 * b4 + a5 * b7
    val v5 = a3 * b2 + a4 * b5 + a5 * b8
    val v6 = a6 * b0 + a7 * b3 + a8 * b6
    val v7 = a6 * b1 + a7 * b4 + a8 * b7
    val v8 = a6 * b2 + a7 * b5 + a8 * b8
    this[SCALE_X] = v0
    this[SKEW_X] = v1
    this[TRANS_X] = v2
    this[SKEW_Y] = v3
    this[SCALE_Y] = v4
    this[TRANS_Y] = v5
    this[PERSP_0] = v6
    this[PERSP_1] = v7
    this[PERSP_2] = v8
}

fun DoubleArray.isFinite(): Boolean = all { it.isFinite() }
