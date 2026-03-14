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

internal fun DoubleArray.invert(): DoubleArray? {
    val v = this

    val mask = computeTypeMask()

    if (isIdentity()) {
        return null
    }

    // Optimized invert for only scale and/or translation matrices.
    if (mask and (TypeMask.SCALE_MASK.mask or TypeMask.TRANSLATE_MASK.mask).inv() == 0) {
        if (mask and TypeMask.SCALE_MASK.mask != 0) {
            // Scale + (optional) Translate
            val invSX = 1.0 / v[SCALE_X]
            val invSY = 1.0 / v[SCALE_Y]
            // Denormalized (non-zero) scale factors will overflow when inverted, in which case
            // the inverse matrix would not be finite, so return false.
            if (!doubleArrayOf(invSX, invSY).isFinite()) {
                return null
            }
            val invTX = -v[SCALE_X] * invSX
            val invTY = -v[SCALE_Y] * invSY
            // Make sure inverse translation didn't overflow/underflow after dividing by scale.
            // Also catches cases where the original matrix's translation values are not finite.
            if (!doubleArrayOf(invTX, invTY).isFinite()) {
                return null
            }

            val dst = DoubleArray(THREE_BY_THREE)

            dst[SCALE_X] = 0.0
            dst[SKEW_X] = 0.0
            dst[PERSP_0] = 0.0
            dst[PERSP_1] = 0.0

            dst[SCALE_X] = invSX
            dst[SCALE_Y] = invSY
            dst[PERSP_2] = 1.0
            dst[TRANS_X] = invTX
            dst[TRANS_Y] = invTY

            return dst
        }

        // Translate-only
        if (!doubleArrayOf(v[TRANS_X], v[TRANS_Y]).isFinite()) {
            // Translation components aren't finite, so inverse isn't possible
            return null
        }
        val dst = DoubleArray(THREE_BY_THREE)
        dst.setTranslate(-v[TRANS_X], -v[TRANS_Y])
        return dst
    }

    val isPersp = mask and TypeMask.PERSPECTIVE_MASK.mask != 0
    val invDet = v.invDeterminant(isPersp)

    if (invDet == 0.0) { // underflow
        return null
    }

    val inv = v.computeInv(invDet, isPersp)
    if (!inv.isFinite()) {
        return null
    }
    return inv
}

internal fun DoubleArray.setPolyToPoly(
    src: FloatArray,
    srcIndex: Int,
    dst: FloatArray,
    dstIndex: Int,
    pointCount: Int,
): Boolean {
    if (pointCount !in 0..4) {
        throw IllegalArgumentException("pointCount must be between 0 and 4")
    }

    if (pointCount == 0) {
        reset()
        return true
    }
    if (pointCount == 1) {
        setTranslate(dst[dstIndex] - src[srcIndex], dst[dstIndex + 1] - src[srcIndex + 1])
        return true
    }

    val srcPoints = src.slice(srcIndex until srcIndex + pointCount * 2).map { it.toDouble() }
    val dstPoints = dst.slice(dstIndex until dstIndex + pointCount * 2).map { it.toDouble() }

    val result = polyToPoly(srcPoints.toDoubleArray(), dstPoints.toDoubleArray())
    if (result != null) {
        result.copyInto(this)
        return true
    }
    return false
}

internal fun polyToPoly(
    src: DoubleArray,
    dst: DoubleArray,
): DoubleArray? {
    val tempMap = DoubleArray(THREE_BY_THREE)
    if (!polyToPolyProc(src, tempMap)) {
        return null
    }
    val inverse = tempMap.invert() ?: return null
    if (!polyToPolyProc(dst, tempMap)) {
        return null
    }
    tempMap.preConcat(inverse)
    return tempMap
}

private fun polyToPolyProc(
    src: DoubleArray,
    dst: DoubleArray,
): Boolean {
    // we have an array of doubles,
    // 4 doubles -> 2  points, 6 doubles -> 3 points, 8 doubles -> 4 points
    return when (src.size) {
        4 -> poly2Proc(src, dst)
        6 -> poly3Proc(src, dst)
        8 -> poly4Proc(src, dst)
        else -> false
    }
}

private fun poly2Proc(
    src: DoubleArray,
    dst: DoubleArray,
): Boolean {
    dst[SCALE_X] = src[1 * 2 + 1] - src[0 * 2 + 1]
    dst[SKEW_Y] = src[0 * 2] - src[1 * 2]
    dst[PERSP_0] = 0.0

    dst[SKEW_X] = src[1 * 2] - src[0 * 2]
    dst[SCALE_Y] = src[1 * 2 + 1] - src[0 * 2 + 1]
    dst[PERSP_1] = 0.0

    dst[TRANS_X] = src[0]
    dst[TRANS_Y] = src[0 + 1]
    dst[PERSP_2] = 1.0
    return true
}

private fun poly3Proc(
    src: DoubleArray,
    dst: DoubleArray,
): Boolean {
    dst[SCALE_X] = src[2 * 2] - src[0 * 2]
    dst[SKEW_Y] = src[2 * 2 + 1] - src[0 * 2 + 1]
    dst[PERSP_0] = 0.0

    dst[SKEW_X] = src[1 * 2] - src[0 * 2]
    dst[SCALE_Y] = src[1 * 2 + 1] - src[0 * 2 + 1]
    dst[PERSP_1] = 0.0

    dst[TRANS_X] = src[0]
    dst[TRANS_Y] = src[0 + 1]
    dst[PERSP_2] = 1.0
    return true
}

private fun poly4Proc(
    src: DoubleArray,
    dst: DoubleArray,
): Boolean {
    var a1: Double
    var a2: Double

    val x0 = src[2 * 2] - src[0 * 2]
    val y0 = src[2 * 2 + 1] - src[0 * 2 + 1]
    val x1 = src[2 * 2] - src[1 * 2]
    val y1 = src[2 * 2 + 1] - src[1 * 2 + 1]
    val x2 = src[2 * 2] - src[3 * 2]
    val y2 = src[2 * 2 + 1] - src[3 * 2 + 1]

    // check if abs(x2) > abs(y2)
    if (fastAbs(x2) > fastAbs(y2)) {
        val denom = ieeeDivide(x1 * y2, x2) - y1
        if (checkForZero(denom)) {
            return false
        }
        a1 = (((x0 - x1) * y2 / x2) - y0 + y1) / denom
    } else {
        val denom = x1 - ieeeDivide(y1 * x2, y2)
        if (checkForZero(denom)) {
            return false
        }
        a1 = (x0 - x1 - ieeeDivide((y0 - y1) * x2, y2)) / denom
    }

    // check if abs(x1) > abs(y1)
    if (fastAbs(x1) > fastAbs(y1)) {
        val denom = y2 - ieeeDivide(x2 * y1, x1)
        if (checkForZero(denom)) {
            return false
        }
        a2 = (y0 - y2 - ieeeDivide((x0 - x2) * y1, x1)) / denom
    } else {
        val denom = ieeeDivide(y2 * x1, y1) - x2
        if (checkForZero(denom)) {
            return false
        }
        a2 = (ieeeDivide((y0 - y2) * x1, y1) - x0 + x2) / denom
    }

    dst[SCALE_X] = a2 * src[3 * 2] + src[3 * 2] - src[0 * 2]
    dst[SKEW_Y] = a2 * src[3 * 2 + 1] + src[3 * 2 + 1] - src[0 * 2 + 1]
    dst[PERSP_0] = a2

    dst[SKEW_X] = a1 * src[1 * 2] + src[1 * 2] - src[0 * 2]
    dst[SCALE_Y] = a1 * src[1 * 2 + 1] + src[1 * 2 + 1] - src[0 * 2 + 1]
    dst[PERSP_1] = a1

    dst[TRANS_X] = src[0 * 2]
    dst[TRANS_Y] = src[0 * 2 + 1]
    dst[PERSP_2] = 1.0
    return true
}

fun ieeeDivide(
    a: Double,
    b: Double,
): Double = a / b

fun checkForZero(a: Double): Boolean = a * a == 0.0
