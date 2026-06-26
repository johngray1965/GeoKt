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
 */

package io.legere.geokt

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KtMatrixInvertPolyTest {
    private val tol = 1e-6

    /** Maps a point through the matrix and asserts it lands ~at (ex, ey). */
    private fun KtImmutableMatrix.assertMaps(
        x: Double,
        y: Double,
        ex: Double,
        ey: Double,
    ) {
        val p = doubleArrayOf(x, y)
        mapPoints(p)
        assertTrue(kotlin.math.abs(p[0] - ex) < tol && kotlin.math.abs(p[1] - ey) < tol, "($x,$y)->(${p[0]},${p[1]}) expected ($ex,$ey)")
    }

    // ---- invert ----

    @Test
    fun identityInvertIsNull() {
        assertThat(KtImmutableMatrix().invert()).isNull()
        // mutable invert(target) returns false on identity.
        assertThat(KtMatrix().invert(KtMatrix())).isEqualTo(false)
    }

    @Test
    fun scaleInverse() {
        val inv = KtImmutableMatrix().setScale(2.0, 4.0).invert()
        assertThat(inv).isNotNull()
        inv!!.assertMaps(2.0, 4.0, 1.0, 1.0) // undoes the scale
    }

    @Test
    fun translateInverse() {
        val inv = KtImmutableMatrix().setTranslate(10.0, 20.0).invert()
        assertThat(inv).isNotNull()
        inv!!.assertMaps(10.0, 20.0, 0.0, 0.0)
    }

    @Test
    fun scaleTranslateInverse() {
        val m = KtMatrix().apply { postScale(2.0, 4.0); postTranslate(10.0, 20.0) }.toImmutable()
        val inv = m.invert()!!
        // m maps (1,1)->(12,24); inv must map (12,24)->(1,1).
        inv.assertMaps(12.0, 24.0, 1.0, 1.0)
    }

    @Test
    fun affineRotateInverseRoundTrips() {
        // Rotation has skew components -> affine path (determinant/computeInv non-perspective).
        val m = KtMatrix().postRotate(37.0).toImmutable()
        val inv = m.invert()!!
        val p = doubleArrayOf(3.0, 5.0)
        m.mapPoints(p)
        inv.mapPoints(p)
        assertTrue(kotlin.math.abs(p[0] - 3.0) < tol && kotlin.math.abs(p[1] - 5.0) < tol)
    }

    @Test
    fun perspectiveInverseRoundTrips() {
        // A perspective matrix -> determinant/computeInv perspective path.
        val m = KtImmutableMatrix(doubleArrayOf(1.0, 0.2, 5.0, 0.1, 1.0, 7.0, 0.0005, 0.0003, 1.0))
        val inv = m.invert()
        assertThat(inv).isNotNull()
        val p = doubleArrayOf(11.0, 13.0)
        m.mapPoints(p)
        inv!!.mapPoints(p)
        assertTrue(kotlin.math.abs(p[0] - 11.0) < 1e-4 && kotlin.math.abs(p[1] - 13.0) < 1e-4)
    }

    @Test
    fun singularMatrixInvertIsNull() {
        // det = 1*1 - 1*1 = 0 (collinear affine) -> invDeterminant underflow -> null.
        val singular = KtImmutableMatrix(doubleArrayOf(1.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0))
        assertThat(singular.invert()).isNull()
    }

    // ---- setPolyToPoly ----

    @Test
    fun polyZeroResetsToIdentity() {
        val m = KtMatrix().apply { postScale(3.0, 3.0) }
        assertThat(m.setPolyToPoly(floatArrayOf(), 0, floatArrayOf(), 0, 0)).isTrue()
        assertThat(m.isIdentity()).isTrue()
    }

    @Test
    fun polyOneTranslates() {
        val m = KtImmutableMatrix().setPolyToPoly(floatArrayOf(1f, 1f), 0, floatArrayOf(4f, 6f), 0, 1)
        assertThat(m).isNotNull()
        m!!.assertMaps(1.0, 1.0, 4.0, 6.0)
        m.assertMaps(0.0, 0.0, 3.0, 5.0) // pure translate by (3,5)
    }

    @Test
    fun polyTwoMapsSegment() {
        val src = floatArrayOf(0f, 0f, 1f, 0f)
        val dst = floatArrayOf(10f, 10f, 10f, 20f) // rotate+scale the segment
        val m = KtImmutableMatrix().setPolyToPoly(src, 0, dst, 0, 2)!!
        m.assertMaps(0.0, 0.0, 10.0, 10.0)
        m.assertMaps(1.0, 0.0, 10.0, 20.0)
    }

    @Test
    fun polyThreeMapsTriangle() {
        val src = floatArrayOf(0f, 0f, 1f, 0f, 0f, 1f)
        val dst = floatArrayOf(5f, 5f, 7f, 5f, 5f, 9f)
        val m = KtImmutableMatrix().setPolyToPoly(src, 0, dst, 0, 3)!!
        m.assertMaps(0.0, 0.0, 5.0, 5.0)
        m.assertMaps(1.0, 0.0, 7.0, 5.0)
        m.assertMaps(0.0, 1.0, 5.0, 9.0)
    }

    @Test
    fun polyFourMapsQuadWideAndTall() {
        val src = floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f) // unit square
        // Wide destination quad (exercises one abs-branch pair in poly4Proc).
        val wide = floatArrayOf(0f, 0f, 10f, 1f, 11f, 6f, 1f, 5f)
        val mw = KtImmutableMatrix().setPolyToPoly(src, 0, wide, 0, 4)!!
        mw.assertMaps(0.0, 0.0, 0.0, 0.0)
        mw.assertMaps(1.0, 0.0, 10.0, 1.0)
        mw.assertMaps(1.0, 1.0, 11.0, 6.0)
        mw.assertMaps(0.0, 1.0, 1.0, 5.0)
        // Tall destination quad (exercises the other abs-branch pair).
        val tall = floatArrayOf(0f, 0f, 1f, 10f, 6f, 11f, 5f, 1f)
        val mt = KtImmutableMatrix().setPolyToPoly(src, 0, tall, 0, 4)!!
        mt.assertMaps(0.0, 0.0, 0.0, 0.0)
        mt.assertMaps(1.0, 0.0, 1.0, 10.0)
        mt.assertMaps(1.0, 1.0, 6.0, 11.0)
        mt.assertMaps(0.0, 1.0, 5.0, 1.0)
    }

    @Test
    fun polyCountOutOfRangeThrows() {
        assertFailsWith<IllegalArgumentException> {
            KtMatrix().setPolyToPoly(FloatArray(10), 0, FloatArray(10), 0, 5)
        }
    }

    @Test
    fun polyDegenerateReturnsNull() {
        // Collinear source points -> poly3Proc maps to a singular matrix -> invert null -> null.
        val src = floatArrayOf(0f, 0f, 1f, 1f, 2f, 2f)
        val dst = floatArrayOf(0f, 0f, 1f, 0f, 0f, 1f)
        assertThat(KtImmutableMatrix().setPolyToPoly(src, 0, dst, 0, 3)).isNull()
    }

    @Test
    fun ieeeDivideAndCheckForZero() {
        assertThat(ieeeDivide(6.0, 2.0)).isEqualTo(3.0)
        assertThat(checkForZero(0.0)).isTrue()
        assertThat(checkForZero(1e-300)).isEqualTo(true) // (1e-300)^2 underflows to 0.0
    }

    @Test
    fun nearlyZeroAndKOrableMasks() {
        assertThat(nearlyZero(0.0)).isTrue() // default-tolerance overload
        assertThat(nearlyZero(1.0)).isEqualTo(false)
        assertThat(kORableMasks).isEqualTo(0x0F) // translate|scale|affine|perspective
    }

    @Test
    fun scaleOverflowInvertIsNull() {
        // A subnormal scale inverts to a non-finite factor -> the isFinite guard returns null.
        assertThat(KtImmutableMatrix().setScale(Double.MIN_VALUE, 1.0).invert()).isNull()
    }

    @Test
    fun polyFourDegenerateReturnsNull() {
        // A destination quad with three collinear points makes poly4Proc's denom zero -> false -> null.
        val src = floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f)
        val dst = floatArrayOf(0f, 0f, 1f, 1f, 2f, 2f, 3f, 3f) // all collinear
        assertThat(KtImmutableMatrix().setPolyToPoly(src, 0, dst, 0, 4)).isNull()
    }
}
