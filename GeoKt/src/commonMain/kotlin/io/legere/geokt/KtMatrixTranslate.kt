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

internal fun DoubleArray.setTranslate(
    dx: Float,
    dy: Float,
) {
    setTranslate(dx.toDouble(), dy.toDouble())
}

internal fun DoubleArray.setTranslate(
    dx: Double,
    dy: Double,
) {
    reset()
    this[TRANS_X] = dx
    this[TRANS_Y] = dy
}

/**
 *        Given:
 *
 *                      | A B C |               | 1 0 dx |
 *             Matrix = | D E F |,  T(dx, dy) = | 0 1 dy |
 *                      | G H I |               | 0 0  1 |
 *
 *         sets SkMatrix to:
 *
 *                                  | A B C | | 1 0 dx |   | A B A*dx+B*dy+C |
 *             Matrix * T(dx, dy) = | D E F | | 0 1 dy | = | D E D*dx+E*dy+F |
 *                                  | G H I | | 0 0  1 |   | G H G*dx+H*dy+I |
 *
 */
internal fun DoubleArray.preTranslate(
    dx: Float,
    dy: Float,
) {
    preTranslate(dx.toDouble(), dy.toDouble())
}

internal fun DoubleArray.preTranslate(
    dx: Double,
    dy: Double,
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

    this[TRANS_X] = a * dx + b * dy + c
    this[TRANS_Y] = d * dx + e * dy + f
    this[PERSP_2] = g * dx + h * dy + i
}

/**
 *         Given:
 *
 *                      | J K L |               | 1 0 dx |
 *             Matrix = | M N O |,  T(dx, dy) = | 0 1 dy |
 *                      | P Q R |               | 0 0  1 |
 *
 *         sets SkMatrix to:
 *
 *                                  | 1 0 dx | | J K L |   | J+dx*P K+dx*Q L+dx*R |
 *             T(dx, dy) * Matrix = | 0 1 dy | | M N O | = | M+dy*P N+dy*Q O+dy*R |
 *                                  | 0 0  1 | | P Q R |   |      P      Q      R |
 *
 */
internal fun DoubleArray.postTranslate(
    dx: Float,
    dy: Float,
) {
    postTranslate(dx.toDouble(), dy.toDouble())
}

internal fun DoubleArray.postTranslate(
    dx: Double,
    dy: Double,
) {
    val j = this[SCALE_X]
    val k = this[SKEW_X]
    val l = this[TRANS_X]
    val m = this[SKEW_Y]
    val n = this[SCALE_Y]
    val o = this[TRANS_Y]
    val p = this[PERSP_0]
    val q = this[PERSP_1]
    val r = this[PERSP_2]

    this[SCALE_X] = j + dx * p
    this[SKEW_X] = k + dx * q
    this[TRANS_X] = l + dx * r
    this[SKEW_Y] = m + dy * p
    this[SCALE_Y] = n + dy * q
    this[TRANS_Y] = o + dy * r
}
