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

enum class TypeMask(
    val mask: Int,
) {
    IDENTITY_MASK(0), // !< identity SkMatrix; all bits clear
    TRANSLATE_MASK(0x01), // !< translation SkMatrix
    SCALE_MASK(0x02), // !< scale SkMatrix
    AFFINE_MASK(0x04), // !< skew or rotate SkMatrix
    PERSPECTIVE_MASK(0x08), // !< perspective SkMatrix
}

enum class Shift(
    val shift: Int,
) {
    TRANSLATE_SHIFT(0),
    SCALE_SHIFT(1),
    AFFINE_SHIFT(2),
    PERSPECTIVE_SHIFT(3),
    RECT_STAYS_RECT_SHIFT(4),
}

val kORableMasks =
    TypeMask.TRANSLATE_MASK.mask or
        TypeMask.SCALE_MASK.mask or
        TypeMask.AFFINE_MASK.mask or
        TypeMask.PERSPECTIVE_MASK.mask

const val SCALAR_ONE_INT = 0x3f800000L

fun DoubleArray.computeTypeMask(): Int {
    val fMat = this
    var mask = 0

    if (fMat[PERSP_0] != 0.0 || fMat[PERSP_1] != 0.0 || fMat[PERSP_2] != 1.0) {
        return kORableMasks
    }

    if (fMat[TRANS_X] != 0.0 || fMat[TRANS_Y] != 0.0) {
        mask = mask or TypeMask.TRANSLATE_MASK.mask
    }

    val m00 = fMat[TRANS_X].toBits()
    var m01 = fMat[SKEW_X].toBits()
    var m10 = fMat[SKEW_Y].toBits()
    val m11 = fMat[TRANS_Y].toBits()

    if (m01 or m10 != 0L) {
        // The skew components may be scale-inducing, unless we are dealing
        // with a pure rotation.  Testing for a pure rotation is expensive,
        // so we opt for being conservative by always setting the scale bit.
        // along with affine.
        // By doing this, we are also ensuring that matrices have the same
        // type masks as their inverses.
        mask = mask or TypeMask.AFFINE_MASK.mask or TypeMask.SCALE_MASK.mask

        // For rectStaysRect, in the affine case, we only need check that
        // the primary diagonal is all zeros and that the secondary diagonal
        // is all non-zero.

        // map non-zero to 1
        m01 = if (m01 != 0L) 1 else 0
        m10 = if (m10 != 0L) 1 else 0

        val dp0 = if (0L == (m00 or m11)) 1 else 0 // true if both are 0
        val ds1 = if (m01 and m10 != 0L) 1 else 0 // true if both are 1

        mask = mask or (dp0 and ds1) shl Shift.RECT_STAYS_RECT_SHIFT.shift
    } else {
        // Only test for scale explicitly if not affine, since affine sets the
        // scale bit.
        if ((m00 xor SCALAR_ONE_INT) or (m11 xor SCALAR_ONE_INT) != 0L) {
            mask = mask or TypeMask.SCALE_MASK.mask
        }

        // Not affine, therefore we already know secondary diagonal is
        // all zeros, so we just need to check that primary diagonal is
        // all non-zero.

        // map non-zero to 1
        val m0 = if (m00 != 0L) 1 else 0
        val m1 = if (m11 != 0L) 1 else 0

        // record if the (p)rimary diagonal is all non-zero
        mask = mask or (m0 and m1) shl Shift.RECT_STAYS_RECT_SHIFT.shift
    }

    return mask
}

internal fun dcross(
    a: Double,
    b: Double,
    c: Double,
    d: Double,
): Double = a * b - c * d

internal fun DoubleArray.determinant(isPerspective: Boolean): Double {
    val mat = this
    if (isPerspective) {
        return mat[SCALE_X] *
            dcross(
                mat[SCALE_Y],
                mat[PERSP_2],
                mat[TRANS_Y],
                mat[PERSP_1],
            ) +
            mat[SKEW_X] *
            dcross(
                mat[TRANS_Y],
                mat[PERSP_0],
                mat[SKEW_Y],
                mat[PERSP_2],
            ) +
            mat[TRANS_X] *
            dcross(
                mat[SKEW_Y],
                mat[PERSP_1],
                mat[SCALE_Y],
                mat[PERSP_0],
            )
    } else {
        return dcross(
            mat[SCALE_X],
            mat[SCALE_Y],
            mat[SKEW_X],
            mat[SKEW_Y],
        )
    }
}

fun nearlyZero(
    value: Double,
    tolerance: Double = ZERO_TOLERANCE,
): Boolean = value < tolerance && value > -tolerance

internal fun DoubleArray.invDeterminant(isPerspective: Boolean): Double {
    val det = determinant(isPerspective)

    // Since the determinant is on the order of the cube of the matrix members,
    // compare to the cube of the default nearly-zero constant (although an
    // estimate of the condition number would be better if it wasn't so expensive).
    if (nearlyZero(
            det,
            ZERO_TOLERANCE * ZERO_TOLERANCE * ZERO_TOLERANCE,
        )
    ) {
        return 0.0
    }
    return 1.0 / det
}

internal fun dCrossDScale(
    a: Double,
    b: Double,
    c: Double,
    d: Double,
    scale: Double,
): Double = dcross(a, b, c, d) * scale

internal fun DoubleArray.computeInv(
    invDet: Double,
    isPersp: Boolean,
): DoubleArray {
//    SkASSERT(src != dst);
//    SkASSERT(src && dst);

    val src = this
    val dst = DoubleArray(THREE_BY_THREE)

    if (isPersp) {
        dst[SCALE_X] = dCrossDScale(src[SCALE_Y], src[PERSP_2], src[TRANS_Y], src[PERSP_1], invDet)
        dst[SKEW_X] = dCrossDScale(src[TRANS_X], src[PERSP_1], src[SKEW_X], src[PERSP_2], invDet)
        dst[TRANS_X] = dCrossDScale(src[SKEW_X], src[TRANS_Y], src[TRANS_X], src[SCALE_Y], invDet)

        dst[SKEW_Y] = dCrossDScale(src[TRANS_Y], src[PERSP_0], src[SKEW_Y], src[PERSP_2], invDet)
        dst[SCALE_Y] = dCrossDScale(src[SCALE_X], src[PERSP_2], src[TRANS_X], src[PERSP_0], invDet)
        dst[TRANS_Y] = dCrossDScale(src[TRANS_X], src[SKEW_Y], src[SCALE_X], src[TRANS_Y], invDet)

        dst[PERSP_0] = dCrossDScale(src[SKEW_Y], src[PERSP_1], src[SCALE_Y], src[PERSP_0], invDet)
        dst[PERSP_1] = dCrossDScale(src[SKEW_X], src[PERSP_0], src[SCALE_X], src[PERSP_1], invDet)
        dst[PERSP_2] = dCrossDScale(src[SCALE_X], src[SCALE_Y], src[SKEW_X], src[SKEW_Y], invDet)
    } else { // not perspective
        dst[SCALE_X] = src[SCALE_Y] * invDet
        dst[SKEW_X] = -src[SKEW_X] * invDet
        dst[TRANS_X] = dCrossDScale(src[SKEW_X], src[TRANS_Y], src[SCALE_Y], src[TRANS_X], invDet)

        dst[SKEW_Y] = -src[SKEW_Y] * invDet
        dst[SCALE_Y] = src[SCALE_X] * invDet
        dst[TRANS_Y] = dCrossDScale(src[SKEW_Y], src[TRANS_X], src[SCALE_X], src[TRANS_Y], invDet)

        dst[PERSP_0] = 0.0
        dst[PERSP_1] = 0.0
        dst[PERSP_2] = 1.0
    }
    return dst
}
