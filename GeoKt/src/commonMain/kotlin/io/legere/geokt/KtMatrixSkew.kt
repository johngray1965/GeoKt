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

internal fun DoubleArray.setSkew(
    kx: Float,
    ky: Float,
    px: Float,
    py: Float,
) {
    setSkew(kx.toDouble(), ky.toDouble(), px.toDouble(), py.toDouble())
}

internal fun DoubleArray.setSkew(
    kx: Double,
    ky: Double,
    px: Double,
    py: Double,
) {
    this[SCALE_X] = 1.0
    this[SKEW_X] = kx
    this[SKEW_Y] = ky
    this[SCALE_Y] = 1.0
    this[TRANS_X] = -(kx * py)
    this[TRANS_Y] = -(ky * px)
    this[PERSP_0] = 0.0
    this[PERSP_1] = 0.0
    this[PERSP_2] = 1.0
}

internal fun DoubleArray.setSkew(
    kx: Float,
    ky: Float,
) {
    setSkew(kx.toDouble(), ky.toDouble())
}

internal fun DoubleArray.setSkew(
    kx: Double,
    ky: Double,
) {
    this[SCALE_X] = 1.0
    this[SKEW_X] = kx
    this[SKEW_Y] = ky
    this[SCALE_Y] = 1.0
    this[TRANS_X] = 0.0
    this[TRANS_Y] = 0.0
    this[PERSP_0] = 0.0
    this[PERSP_1] = 0.0
    this[PERSP_2] = 1.0
}

/**
 *         Given:
 *
 *                      | A B C |                       |  1 kx dx |
 *             Matrix = | D E F |,  K(kx, ky, px, py) = | ky  1 dy |
 *                      | G H I |                       |  0  0  1 |
 *
 *         where
 *
 *             dx = -kx * py
 *             dy = -ky * px
 *
 *         sets SkMatrix to:
 *
 *                                          | A B C | |  1 kx dx |   | A+B*ky A*kx+B A*dx+B*dy+C |
 *             Matrix * K(kx, ky, px, py) = | D E F | | ky  1 dy | = | D+E*ky D*kx+E D*dx+E*dy+F |
 *                                          | G H I | |  0  0  1 |   | G+H*ky G*kx+H G*dx+H*dy+I |
 */
internal fun DoubleArray.preSkew(
    kx: Float,
    ky: Float,
    px: Float,
    py: Float,
) {
    preSkew(kx.toDouble(), ky.toDouble(), px.toDouble(), py.toDouble())
}

internal fun DoubleArray.preSkew(
    kx: Double,
    ky: Double,
    px: Double,
    py: Double,
) {
    val dx = -kx * py
    val dy = -ky * px

    val a = this[SCALE_X]
    val b = this[SKEW_X]
    val c = this[TRANS_X]
    val d = this[SKEW_Y]
    val e = this[SCALE_Y]
    val f = this[TRANS_Y]
    val g = this[PERSP_0]
    val h = this[PERSP_1]
    val i = this[PERSP_2]

    this[SCALE_X] = a + b * ky
    this[SKEW_X] = a * kx + b
    this[TRANS_X] = a * dx + b * dy + c
    this[SKEW_Y] = d + e * ky
    this[SCALE_Y] = d * kx + e
    this[TRANS_Y] = d * dx + e * dy + f
    this[PERSP_0] = g + h * ky
    this[PERSP_1] = g * kx + h
    this[PERSP_2] = g * dx + h * dy + i
}

internal fun DoubleArray.preSkew(
    kx: Float,
    ky: Float,
) {
    preSkew(kx.toDouble(), ky.toDouble())
}

internal fun DoubleArray.preSkew(
    kx: Double,
    ky: Double,
) {
    val a = this[SCALE_X]
    val b = this[SKEW_X]
    val c = this[TRANS_X]
    val d = this[SKEW_Y]
    val e = this[SCALE_Y]
    val f = this[TRANS_Y]
    val g = this[PERSP_0]
    val h = this[PERSP_1]
    val i = this[PERSP_2]

    this[SCALE_X] = a + b * ky
    this[SKEW_X] = a * kx + b
    this[TRANS_X] = c
    this[SKEW_Y] = d + e * ky
    this[SCALE_Y] = d * kx + e
    this[TRANS_Y] = f
    this[PERSP_0] = g + h * ky
    this[PERSP_1] = g * kx + h
    this[PERSP_2] = i
}

// private fun shouldTruncate(value: Double): Boolean =

/**
 *
 *         Given:
 *
 *                      | J K L |                       |  1 kx dx |
 *             Matrix = | M N O |,  K(kx, ky, px, py) = | ky  1 dy |
 *                      | P Q R |                       |  0  0  1 |
 *
 *         where
 *
 *             dx = -kx * py
 *             dy = -ky * px
 *
 *         sets SkMatrix to:
 *
 *                                          | 1 kx dx| |J K L|   |J+kx*M+dx*P K+kx*N+dx*Q L+kx*O+dx+R|
 *             K(kx, ky, px, py) * Matrix = |ky  1 dy| |M N O| = |ky*J+M+dy*P ky*K+N+dy*Q ky*L+O+dy*R|
 *                                          | 0  0  1| |P Q R|   |          P           Q           R|
 *
 *
 */
internal fun DoubleArray.postSkew(
    kx: Float,
    ky: Float,
    px: Float,
    py: Float,
) {
    postSkew(kx.toDouble(), ky.toDouble(), px.toDouble(), py.toDouble())
}

internal fun DoubleArray.postSkew(
    kx: Double,
    ky: Double,
    px: Double,
    py: Double,
) {
    val dx = -kx * py
    val dy = -ky * px

    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]
    val p = this[PERSP_0]
    val q = this[PERSP_1]
    val r = this[PERSP_2]

    this[SCALE_X] = j + kx * m + dx * p
    this[SKEW_X] = k + kx * n + dx * q
    this[TRANS_X] = l + kx * o + dx * r
    this[SKEW_Y] = ky * j + m + dy * p
    this[SCALE_Y] = ky * k + n + dy * q
    this[TRANS_Y] = ky * l + o + dy * r
}

internal fun DoubleArray.postSkew(
    kx: Float,
    ky: Float,
) {
    postSkew(kx.toDouble(), ky.toDouble())
}

internal fun DoubleArray.postSkew(
    kx: Double,
    ky: Double,
) {
    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]

    this[SCALE_X] = j + kx * m
    this[SKEW_X] = k + kx * n
    this[TRANS_X] = l + kx * o
    this[SKEW_Y] = ky * j + m
    this[SCALE_Y] = ky * k + n
    this[TRANS_Y] = ky * l + o
}
