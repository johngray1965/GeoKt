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

internal fun DoubleArray.setScale(
    sx: Float,
    sy: Float,
    px: Float,
    py: Float,
) {
    val sxd = sx.toDouble()
    val syd = sy.toDouble()
    val pxd = px.toDouble()
    val pyd = py.toDouble()
    setScale(sxd, syd, pxd, pyd)
}

internal fun DoubleArray.setScale(
    sx: Double,
    sy: Double,
    px: Double,
    py: Double,
) {
    this[SCALE_X] = sx
    this[SCALE_Y] = sy
    this[TRANS_X] = (px - sx * px)
    this[TRANS_Y] = (py - sy * py)
    this[SKEW_X] = 0.0
    this[SKEW_Y] = 0.0
    this[PERSP_0] = 0.0
    this[PERSP_1] = 0.0
    this[PERSP_2] = 1.0
}

internal fun DoubleArray.setScale(
    sx: Float,
    sy: Float,
) {
    val sxd = sx.toDouble()
    val syd = sy.toDouble()
    setScale(sxd, syd)
}

internal fun DoubleArray.setScale(
    sx: Double,
    sy: Double,
) {
    this[SCALE_X] = sx
    this[SCALE_Y] = sy
    this[TRANS_X] = 0.0
    this[TRANS_Y] = 0.0
    this[SKEW_X] = 0.0
    this[SKEW_Y] = 0.0
    this[PERSP_0] = 0.0
    this[PERSP_1] = 0.0
    this[PERSP_2] = 1.0
}

/**
 *         Given:
 *
 *                      | A B C |                       | sx  0 dx |
 *             Matrix = | D E F |,  S(sx, sy, px, py) = |  0 sy dy |
 *                      | G H I |                       |  0  0  1 |
 *
 *         where
 *
 *             dx = px - sx * px
 *             dy = py - sy * py
 *
 *         sets SkMatrix to:
 *
 *                                          | A B C | | sx  0 dx |   | A*sx B*sy A*dx+B*dy+C |
 *             Matrix * S(sx, sy, px, py) = | D E F | |  0 sy dy | = | D*sx E*sy D*dx+E*dy+F |
 *                                          | G H I | |  0  0  1 |   | G*sx H*sy G*dx+H*dy+I |
 *
 *
 */
internal fun DoubleArray.preScale(
    sx: Float,
    sy: Float,
    px: Float,
    py: Float,
) {
    preScale(sx.toDouble(), sy.toDouble(), px.toDouble(), py.toDouble())
}

internal fun DoubleArray.preScale(
    sx: Double,
    sy: Double,
    px: Double,
    py: Double,
) {
    val dx = px - sx * px
    val dy = py - sy * py

    val a = this[SCALE_X]
    val b = this[SKEW_X]
    val c = this[TRANS_X]
    val d = this[SKEW_Y]
    val e = this[SCALE_Y]
    val f = this[TRANS_Y]
    val g = this[PERSP_0]
    val h = this[PERSP_1]
    val i = this[PERSP_2]

    this[SCALE_X] = a * sx
    this[SKEW_X] = b * sy
    this[TRANS_X] = a * dx + b * dy + c
    this[SKEW_Y] = d * sx
    this[SCALE_Y] = e * sy
    this[TRANS_Y] = d * dx + e * dy + f
    this[PERSP_0] = g * sx
    this[PERSP_1] = h * sy
    this[PERSP_2] = g * dx + h * dy + i
}

internal fun DoubleArray.preScale(
    sx: Float,
    sy: Float,
) {
    preScale(sx.toDouble(), sy.toDouble())
}

internal fun DoubleArray.preScale(
    sx: Double,
    sy: Double,
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

    this[SCALE_X] = a * sx
    this[SKEW_X] = b * sy
    this[TRANS_X] = c
    this[SKEW_Y] = d * sx
    this[SCALE_Y] = e * sy
    this[TRANS_Y] = f
    this[PERSP_0] = g * sx
    this[PERSP_1] = h * sy
    this[PERSP_2] = i
}

/*
        Given:

                     | J K L |                       | sx  0 dx |
            Matrix = | M N O |,  S(sx, sy, px, py) = |  0 sy dy |
                     | P Q R |                       |  0  0  1 |

        where

            dx = px - sx * px
            dy = py - sy * py

        sets SkMatrix to:

                                         | sx  0 dx | | J K L |   | sx*J+dx*P sx*K+dx*Q sx*L+dx+R |
            S(sx, sy, px, py) * Matrix = |  0 sy dy | | M N O | = | sy*M+dy*P sy*N+dy*Q sy*O+dy*R |
                                         |  0  0  1 | | P Q R |   |         P         Q         R |

 */
internal fun DoubleArray.postScale(
    sx: Float,
    sy: Float,
    px: Float,
    py: Float,
) {
    postScale(sx.toDouble(), sy.toDouble(), px.toDouble(), py.toDouble())
}

internal fun DoubleArray.postScale(
    sx: Double,
    sy: Double,
    px: Double,
    py: Double,
) {
    val dx = px - sx * px
    val dy = py - sy * py

    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]
    val p = this[PERSP_0]
    val q = this[PERSP_1]
    val r = this[PERSP_2]

    this[SCALE_X] = sx * j + dx * p
    this[SKEW_X] = sx * k + dx * q
    this[TRANS_X] = sx * l + dx * r
    this[SKEW_Y] = sy * m + dy * p
    this[SCALE_Y] = sy * n + dy * q
    this[TRANS_Y] = sy * o + dy * r
}

internal fun DoubleArray.postScale(
    sx: Float,
    sy: Float,
) {
    postScale(sx.toDouble(), sy.toDouble())
}

internal fun DoubleArray.postScale(
    sx: Double,
    sy: Double,
) {
    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]

    this[SCALE_X] = sx * j
    this[SKEW_X] = sx * k
    this[TRANS_X] = sx * l
    this[SKEW_Y] = sy * m
    this[SCALE_Y] = sy * n
    this[TRANS_Y] = sy * o
}
