package io.legere.geokt

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlin.test.Test

class KtMatrixTest {

    @Test
    fun defaultConstructorCreatesIdentityMatrix() {
        val matrix = KtImmutableMatrix()
        assertThat(matrix.isIdentity()).isTrue()
        assertThat(matrix.isAffine()).isTrue()
    }

    @Test
    fun secondaryConstructorCreatesACopy() {
        val input = KtImmutableMatrix(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))
        val matrix = KtImmutableMatrix(input)
        assertThat(matrix.isIdentity()).isFalse()
        assertThat(matrix.isAffine()).isFalse()
        assertThat(matrix.values).isEqualTo(input.values)
    }

    @Test
    fun mutableDefaultConstructorCreatesIdentityMatrix() {
        val matrix = KtMatrix()
        assertThat(matrix.isIdentity()).isTrue()
        assertThat(matrix.isAffine()).isTrue()
    }

    @Test
    fun mutableSecondaryConstructorCreatesACopy() {
        val input = KtImmutableMatrix(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))
        val matrix = KtMatrix(input)
        assertThat(matrix.isIdentity()).isFalse()
        assertThat(matrix.isAffine()).isFalse()
        assertThat(matrix.values).isEqualTo(input.values)
    }

    @Test
    fun isAffine() {
        assertThat(KtImmutableMatrix(doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)).isAffine()).isFalse()
        assertThat(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                ),
            ).isAffine(),
        ).isTrue()
        assertThat(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    2.0,
                ),
            ).isAffine(),
        ).isFalse()
        assertThat(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    0.0,
                    1.0,
                    1.0,
                ),
            ).isAffine(),
        ).isFalse()
        assertThat(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    1.0,
                    0.0,
                    1.0,
                ),
            ).isAffine(),
        ).isFalse()
    }

    @Test
    fun isIdentity() {
        assertThat(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    2.0,
                    3.0,
                    4.0,
                    5.0,
                    6.0,
                    7.0,
                    8.0,
                    9.0,
                ),
            ).isIdentity(),
        ).isFalse()
        assertThat(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                ),
            ).isIdentity(),
        ).isTrue()
        repeat(9) { column ->
            val startingInput =
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                )
            startingInput[column] += 1.0
            assertThat(KtImmutableMatrix(startingInput).isIdentity()).isFalse()
        }
    }

    @Test
    fun resetSetsMatrixToIdentity() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 20f)
        assertThat(matrix.isIdentity()).isFalse()

        matrix.reset()
        assertThat(matrix.isIdentity()).isTrue()
    }

    @Test
    fun setCopiesValues() {
        val src = KtImmutableMatrix().setTranslate(10f, 20f)
        val dst = KtMatrix()
        dst.set(src)
        assertThat(dst.toImmutable()).isEqualTo(src)
        assertThat(dst.values[TRANS_X]).isEqualTo(10.0)
    }

    @Test
    fun setTranslateSetsCorrectValues() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 20f)

        val values = matrix.values
        assertThat(values[TRANS_X]).isEqualTo(10.0)
        assertThat(values[TRANS_Y]).isEqualTo(20.0)
        assertThat(values[SCALE_X]).isEqualTo(1.0)
        assertThat(values[SCALE_Y]).isEqualTo(1.0)
    }

    @Test
    fun setScaleSetsCorrectValues() {
        val matrix = KtMatrix()
        matrix.setScale(2f, 3f)

        val values = matrix.values
        assertThat(values[SCALE_X]).isEqualTo(2.0)
        assertThat(values[SCALE_Y]).isEqualTo(3.0)
        assertThat(values[TRANS_X]).isEqualTo(0.0)
        assertThat(values[TRANS_Y]).isEqualTo(0.0)
    }

    @Test
    fun setScaleWithPivotSetsCorrectValues() {
        val matrix = KtMatrix()
        matrix.setScale(2f, 3f, 10f, 10f)
        // px - sx*px = 10 - 2*10 = -10
        // py - sy*py = 10 - 3*10 = -20

        val values = matrix.values
        assertThat(values[SCALE_X]).isEqualTo(2.0)
        assertThat(values[SCALE_Y]).isEqualTo(3.0)
        assertThat(values[TRANS_X]).isEqualTo(-10.0)
        assertThat(values[TRANS_Y]).isEqualTo(-20.0)
    }

    @Test
    fun setRotateSetsCorrectValuesFor0() {
        val matrix = KtMatrix()
        matrix.setRotate(0f)

        val values = matrix.values
        // cos(90) = 0, sin(90) = 1
        assertThat(values[SCALE_X]).isCloseTo(1.0, 0.0001)
        assertThat(values[SKEW_X]).isCloseTo(0.0, 0.0001)
        assertThat(values[SKEW_Y]).isCloseTo(0.0, 0.0001)
        assertThat(values[SCALE_Y]).isCloseTo(1.0, 0.0001)
    }

    @Test
    fun setRotateSetsCorrectValuesFor45() {
        val matrix = KtMatrix()
        matrix.setRotate(45f)

        val values = matrix.values
        // cos(90) = 0, sin(90) = 1
        assertThat(values[SCALE_X]).isCloseTo(0.7071067811865476, 0.0001)
        assertThat(values[SKEW_X]).isCloseTo(-0.7071067811865476, 0.0001)
        assertThat(values[SKEW_Y]).isCloseTo(0.7071067811865475, 0.0001)
        assertThat(values[SCALE_Y]).isCloseTo(0.7071067811865475, 0.0001)
    }

    @Test
    fun setRotateSetsCorrectValuesFor90() {
        val matrix = KtMatrix()
        matrix.setRotate(90f)

        val values = matrix.values
        // cos(90) = 0, sin(90) = 1
        assertThat(values[SCALE_X]).isCloseTo(0.0, 0.0001)
        assertThat(values[SKEW_X]).isCloseTo(-1.0, 0.0001)
        assertThat(values[SKEW_Y]).isCloseTo(1.0, 0.0001)
        assertThat(values[SCALE_Y]).isCloseTo(0.0, 0.0001)
    }

    @Test
    fun setRotateSetsCorrectValuesFor180() {
        val matrix = KtMatrix()
        matrix.setRotate(180f)

        val values = matrix.values
        // cos(90) = 0, sin(90) = 1
        assertThat(values[SCALE_X]).isCloseTo(-1.0, 0.0001)
        assertThat(values[SKEW_X]).isCloseTo(0.0, 0.0001)
        assertThat(values[SKEW_Y]).isCloseTo(0.0, 0.0001)
        assertThat(values[SCALE_Y]).isCloseTo(-1.0, 0.0001)
    }

    @Test
    fun setRotateSetsCorrectValuesFor270() {
        val matrix = KtMatrix()
        matrix.setRotate(270f)

        val values = matrix.values
        // cos(90) = 0, sin(90) = 1
        assertThat(values[SCALE_X]).isCloseTo(0.0, 0.0001)
        assertThat(values[SKEW_X]).isCloseTo(1.0, 0.0001)
        assertThat(values[SKEW_Y]).isCloseTo(-1.0, 0.0001)
        assertThat(values[SCALE_Y]).isCloseTo(0.0, 0.0001)
    }

    @Test
    fun setRotateSetsCorrectValues() {
        val matrix = KtMatrix()
        matrix.setRotate(90f)

        val values = matrix.values
        // cos(90) = 0, sin(90) = 1
        assertThat(values[SCALE_X]).isCloseTo(0.0, 0.0001)
        assertThat(values[SKEW_X]).isCloseTo(-1.0, 0.0001)
        assertThat(values[SKEW_Y]).isCloseTo(1.0, 0.0001)
        assertThat(values[SCALE_Y]).isCloseTo(0.0, 0.0001)
    }

    @Test
    fun setSkewSetsCorrectValues() {
        val matrix = KtMatrix()
        matrix.setSkew(0.5f, 0.5f)

        val values = matrix.values
        assertThat(values[SKEW_X]).isEqualTo(0.5)
        assertThat(values[SKEW_Y]).isEqualTo(0.5)
        assertThat(values[SCALE_X]).isEqualTo(1.0)
    }

    @Test
    fun concatMultipliesMatrices() {
        // A: translate(10, 0)
        val a = KtImmutableMatrix().setTranslate(10f, 0f)
        // B: scale(2, 1)
        val b = KtImmutableMatrix().setScale(2f, 1f)

        // C = A * B
        // v' = A * (B * v)
        val c = a.preConcat(b)

        assertThat(c.values[SCALE_X]).isEqualTo(2.0)
        assertThat(c.values[TRANS_X]).isEqualTo(10.0)
    }

    @Test
    fun preTranslateModifiesMatrixCorrectly() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 10f)
        matrix.preTranslate(5f, 5f)

        val values = matrix.values
        assertThat(values[TRANS_X]).isEqualTo(15.0)
        assertThat(values[TRANS_Y]).isEqualTo(15.0)
    }

    @Test
    fun postTranslateModifiesMatrixCorrectly() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 10f)
        matrix.postTranslate(5f, 5f)

        val values = matrix.values
        assertThat(values[TRANS_X]).isEqualTo(15.0)
        assertThat(values[TRANS_Y]).isEqualTo(15.0)
    }

    @Test
    fun preScaleModifiesMatrixCorrectly() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 10f)
        matrix.preScale(2f, 2f)

        val values = matrix.values
        assertThat(values[SCALE_X]).isEqualTo(2.0)
        assertThat(values[SCALE_Y]).isEqualTo(2.0)
        assertThat(values[TRANS_X]).isEqualTo(10.0)
        assertThat(values[TRANS_Y]).isEqualTo(10.0)
    }

    @Test
    fun postScaleModifiesMatrixCorrectly() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 10f)
        matrix.postScale(2f, 2f)

        val values = matrix.values
        assertThat(values[SCALE_X]).isEqualTo(2.0)
        assertThat(values[SCALE_Y]).isEqualTo(2.0)
        assertThat(values[TRANS_X]).isEqualTo(20.0)
        assertThat(values[TRANS_Y]).isEqualTo(20.0)
    }

    @Test
    fun preSkewModifiesMatrixCorrectly() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 10f)
        matrix.preSkew(1f, 0f)

        val values = matrix.values
        assertThat(values[SCALE_X]).isEqualTo(1.0)
        assertThat(values[SKEW_X]).isEqualTo(1.0)
        assertThat(values[TRANS_X]).isEqualTo(10.0)
    }

    @Test
    fun postSkewModifiesMatrixCorrectly() {
        val matrix = KtMatrix()
        matrix.setTranslate(10f, 10f)
        matrix.postSkew(1f, 0f)

        val values = matrix.values
        assertThat(values[SCALE_X]).isEqualTo(1.0)
        assertThat(values[SKEW_X]).isEqualTo(1.0)
        assertThat(values[TRANS_X]).isEqualTo(20.0)
    }

    @Test
    fun isIdentityChecksAllFields() {
        val matrix = KtMatrix()
        assertThat(matrix.isIdentity()).isTrue()

        matrix.values[PERSP_0] = 0.1
        assertThat(matrix.isIdentity()).isFalse()
    }

    @Test
    fun isAffineChecksPerspectiveFields() {
        val matrix = KtMatrix()
        assertThat(matrix.isAffine()).isTrue()

        matrix.values[PERSP_0] = 0.1
        assertThat(matrix.isAffine()).isFalse()

        matrix.reset()
        matrix.values[PERSP_2] = 0.5
        assertThat(matrix.isAffine()).isFalse()
    }

    @Test
    fun invertCalculatesCorrectInverse() {
        val matrix = KtMatrix()
        matrix.setScale(2f, 2f)
        matrix.postTranslate(10f, 20f)

        val inverse = KtMatrix()
        val success = matrix.invert(inverse)
        assertThat(success).isTrue()

        val product = matrix.toImmutable().preConcat(inverse.toImmutable())
        assertThat(product.isIdentity()).isTrue()
    }

    @Test
    fun invertReturnsFalseForNonInvertibleMatrix() {
        val matrix = KtMatrix()
        matrix.setScale(0f, 2f)
        val inverse = KtMatrix()
        assertThat(matrix.invert(inverse)).isFalse()
    }

    @Test
    fun immutableInvertReturnsNullForNonInvertibleMatrix() {
        val matrix = KtImmutableMatrix().setScale(0f, 2f)
        assertThat(matrix.invert()).isNull()
    }

    @Test
    fun equalsReturnsTrueForEqualMatrices() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtMatrix().setScale(0f, 2f)
        assertThat(matrixA == matrixB).isTrue()
    }

    @Suppress("SENSELESS_COMPARISON", "UnnecessaryVariable")
    @Test
    fun equalsReturnsTrueForTheSameMatrices() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = matrixA
        assertThat(matrixA == matrixB).isTrue()
    }

    @Test
    fun equalsReturnsFalseNotForTheSameMatrices() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtImmutableMatrix().setScale(0f, 2f)
        assertThat(matrixA.equals(matrixB)).isFalse()
        assertThat(matrixB.equals(matrixA)).isFalse()
    }

    @Test
    fun equalsReturnsFalseNotForTheSameMatrices2() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtImmutableMatrix().setScale(0f, 2f)
        assertThat(matrixB.equals(matrixA)).isFalse()
        assertThat(matrixA.equals(matrixB)).isFalse()
    }

    @Suppress("SENSELESS_COMPARISON")
    @Test
    fun equalsReturnsFalseNotForTheSameType() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = Any()
        assertThat(matrixB.equals(matrixA)).isFalse()
        assertThat(matrixA.equals(matrixB)).isFalse()
        assertThat(matrixA.equals(null)).isFalse()
    }

    @Suppress("SENSELESS_COMPARISON")
    @Test
    fun equalsReturnsFalseNotForTheSameType2() {
        val matrixA = KtImmutableMatrix().setScale(0f, 2f)
        val matrixB = Any()
        assertThat(matrixB.equals(matrixA)).isFalse()
        assertThat(matrixA.equals(matrixB)).isFalse()
        assertThat(matrixA.equals(null)).isFalse()
    }

    @Test
    fun equalsReturnsFalseForNonEqualMatrices() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtMatrix().setScale(2f, 0f)
        assertThat(matrixA == matrixB).isFalse()
    }

    @Test
    fun equalsReturnsFalseForNonEqualMatrices2() {
        val matrixA = KtImmutableMatrix().setScale(0f, 2f)
        val matrixB = KtImmutableMatrix().setScale(2f, 0f)
        assertThat(matrixA == matrixB).isFalse()
    }

    @Test
    fun equalsReturnsFalseNotForDifferentTypes() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtImmutableMatrix().setScale(0f, 2f)
        assertThat(matrixA.equals(matrixB)).isFalse()
        assertThat(matrixB.equals(matrixA)).isFalse()
    }

    @Test
    fun hashCodeReturnsTrueForEqualMatrices() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtMatrix().setScale(0f, 2f)
        assertThat(matrixA.hashCode() == matrixB.hashCode()).isTrue()
    }

    @Test
    fun hashCodeReturnsTrueForTheSameMatrices() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = matrixA
        assertThat(matrixA.hashCode() == matrixB.hashCode()).isTrue()
    }

    @Test
    fun hashCodeReturnsFalseForNonEqualMatrices() {
        val matrixA = KtImmutableMatrix().setScale(0f, 2f).postTranslate(10f, 20f).postRotate(45.0)
        val matrixB = KtImmutableMatrix().setScale(3f, 0f)
        val hashA = matrixA.hashCode()
        val hashB = matrixB.hashCode()
        assertThat(hashA).isNotEqualTo(hashB)
    }

    @Test
    fun hashCodeReturnsFalseNotForDifferentTypes() {
        val matrixA = KtMatrix().setScale(0f, 2f)
        val matrixB = KtImmutableMatrix().setScale(0f, 2f)
        assertThat(matrixA.hashCode() == matrixB.hashCode()).isFalse()
    }

    @Test
    fun toMutableReturnsACopy() {
        val matrixA = KtImmutableMatrix().setScale(0f, 2f)
        val matrixB = matrixA.toMutable()
        assertThat(matrixA).isInstanceOf(KtImmutableMatrix::class)
        assertThat(matrixB).isInstanceOf(KtMatrix::class)
        assertThat(matrixA).isNotEqualTo(matrixB)
        assertThat(matrixA.hashCode()).isNotEqualTo(matrixB.hashCode())
        assertThat(matrixA.values).isEqualTo(matrixB.values)
    }

    @Test
    fun identityReturnsACopy() {
        val matrixA = KtImmutableMatrix.IDENTITY
        assertThat(matrixA.values).isEqualTo(KtImmutableMatrix().values)
    }
}
