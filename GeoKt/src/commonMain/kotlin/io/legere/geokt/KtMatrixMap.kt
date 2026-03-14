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

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

internal fun DoubleArray.mapX(
    x: Float,
    y: Float,
): Float {
    val w = this[PERSP_0] * x + this[PERSP_1] * y + this[PERSP_2]
    return ((this[SCALE_X] * x + this[SKEW_X] * y + this[TRANS_X]) / w).toFloat()
}

internal fun DoubleArray.mapY(
    x: Float,
    y: Float,
): Float {
    val w = this[PERSP_0] * x + this[PERSP_1] * y + this[PERSP_2]
    return ((this[SKEW_Y] * x + this[SCALE_Y] * y + this[TRANS_Y]) / w).toFloat()
}

internal fun DoubleArray.mapRect(rect: KtRectF) {
    val x1 = mapX(rect.left, rect.top)
    val y1 = mapY(rect.left, rect.top)
    val x2 = mapX(rect.right, rect.top)
    val y2 = mapY(rect.right, rect.top)
    val x3 = mapX(rect.right, rect.bottom)
    val y3 = mapY(rect.right, rect.bottom)
    val x4 = mapX(rect.left, rect.bottom)
    val y4 = mapY(rect.left, rect.bottom)
    rect.left = min(x1, min(x2, min(x3, x4)))
    rect.top = min(y1, min(y2, min(y3, y4)))
    rect.right = max(x1, max(x2, max(x3, x4)))
    rect.bottom = max(y1, max(y2, max(y3, y4)))
}

internal fun DoubleArray.mapRect(
    dst: KtRectF,
    src: FloatRectValues,
) {
    val x1 = mapX(src.left, src.top)
    val y1 = mapY(src.left, src.top)
    val x2 = mapX(src.right, src.top)
    val y2 = mapY(src.right, src.top)
    val x3 = mapX(src.right, src.bottom)
    val y3 = mapY(src.right, src.bottom)
    val x4 = mapX(src.left, src.bottom)
    val y4 = mapY(src.left, src.bottom)
    dst.set(
        l = min(x1, min(x2, min(x3, x4))),
        t = min(y1, min(y2, min(y3, y4))),
        r = max(x1, max(x2, max(x3, x4))),
        b = max(y1, max(y2, max(y3, y4))),
    )
}

@Suppress("MagicNumber")
internal fun DoubleArray.mapRadius(radius: Float): Float = mapRadius(radius.toDouble()).toFloat()

@Suppress("MagicNumber")
internal fun DoubleArray.mapRadius(radius: Double): Double {
    val tmp = DoubleArray(4)
    tmp[0] = radius
    tmp[1] = 0.0
    tmp[2] = 0.0
    tmp[3] = radius
    mapVectors(tmp)

    val d1 = distance(0.0, 0.0, tmp[0], tmp[1])
    val d2 = distance(0.0, 0.0, tmp[2], tmp[3])
    return sqrt(d1 * d2)
}

internal fun distance(
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
): Double {
    val dx = x1 - x2
    val dy = y1 - y2
    return sqrt(dx * dx + dy * dy)
}

internal fun DoubleArray.mapPoints(pts: FloatArray) {
    mapPoints(pts, 0, pts, 0, pts.size / 2)
}

internal fun DoubleArray.mapPoints(
    dst: FloatArray,
    src: FloatArray,
) {
    mapPoints(dst, 0, src, 0, src.size / 2)
}

internal fun DoubleArray.mapPoints(pts: DoubleArray) {
    mapPoints(pts, 0, pts, 0, pts.size / 2)
}

internal fun DoubleArray.mapPoints(
    dst: DoubleArray,
    src: DoubleArray,
) {
    mapPoints(dst, 0, src, 0, src.size / 2)
}

internal fun DoubleArray.mapPoints(
    dst: FloatArray,
    dstIndex: Int,
    src: FloatArray,
    srcIndex: Int,
    pointCount: Int,
) {
    for (i in 0 until pointCount) {
        val si = srcIndex + i * 2
        val di = dstIndex + i * 2
        val x = src[si]
        val y = src[si + 1]
        val w = this[PERSP_0] * x + this[PERSP_1] * y + this[PERSP_2]
        dst[di] = ((this[SCALE_X] * x + this[SKEW_X] * y + this[TRANS_X]) / w).toFloat()
        dst[di + 1] = ((this[SKEW_Y] * x + this[SCALE_Y] * y + this[TRANS_Y]) / w).toFloat()
    }
}

internal fun DoubleArray.mapPoints(
    dst: DoubleArray,
    dstIndex: Int,
    src: DoubleArray,
    srcIndex: Int,
    pointCount: Int,
) {
    for (i in 0 until pointCount) {
        val si = srcIndex + i * 2
        val di = dstIndex + i * 2
        val x = src[si]
        val y = src[si + 1]
        val w = this[PERSP_0] * x + this[PERSP_1] * y + this[PERSP_2]
        dst[di] = ((this[SCALE_X] * x + this[SKEW_X] * y + this[TRANS_X]) / w)
        dst[di + 1] = ((this[SKEW_Y] * x + this[SCALE_Y] * y + this[TRANS_Y]) / w)
    }
}

internal fun DoubleArray.mapVectors(vecs: FloatArray) {
    mapVectors(vecs, 0, vecs, 0, vecs.size / 2)
}

internal fun DoubleArray.mapVectors(vecs: DoubleArray) {
    mapVectors(vecs, 0, vecs, 0, vecs.size / 2)
}

internal fun DoubleArray.mapVectors(
    dest: FloatArray,
    src: FloatArray,
) {
    mapVectors(dest, 0, src, 0, src.size / 2)
}

internal fun DoubleArray.mapVectors(
    dest: DoubleArray,
    src: DoubleArray,
) {
    mapVectors(dest, 0, src, 0, src.size / 2)
}

internal fun DoubleArray.mapVectors(
    dst: FloatArray,
    dstIndex: Int,
    src: FloatArray,
    srcIndex: Int,
    vectorCount: Int,
) {
    for (i in 0 until vectorCount) {
        val si = srcIndex + i * 2
        val di = dstIndex + i * 2
        val x = src[si]
        val y = src[si + 1]
        dst[di] = (this[SCALE_X] * x + this[SKEW_X] * y).toFloat()
        dst[di + 1] = (this[SKEW_Y] * x + this[SCALE_Y] * y).toFloat()
    }
}

internal fun DoubleArray.mapVectors(
    dst: DoubleArray,
    dstIndex: Int,
    src: DoubleArray,
    srcIndex: Int,
    vectorCount: Int,
) {
    for (i in 0 until vectorCount) {
        val si = srcIndex + i * 2
        val di = dstIndex + i * 2
        val x = src[si]
        val y = src[si + 1]
        dst[di] = (this[SCALE_X] * x + this[SKEW_X] * y)
        dst[di + 1] = (this[SKEW_Y] * x + this[SCALE_Y] * y)
    }
}
