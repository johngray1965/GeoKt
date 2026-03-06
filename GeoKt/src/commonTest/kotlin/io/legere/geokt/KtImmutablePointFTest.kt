package io.legere.geokt
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlin.test.Test

class KtImmutablePointFTest {
    @Test
    fun positiveFloatValuesConversion() {
        // Verify that a  KtImmutablePointF with positive x and y float values is correctly converted to a FloatArray.
        val point =  KtImmutablePointF(10.5f, 20.5f)
        val expectedArray = floatArrayOf(10.5f, 20.5f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun negativeFloatValuesConversion() {
        // Verify that a  KtImmutablePointF with negative x and y float values is correctly converted to a FloatArray.
        val point =  KtImmutablePointF(-10.5f, -20.5f)
        val expectedArray = floatArrayOf(-10.5f, -20.5f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun zeroFloatValuesConversion() {
        // Verify that a  KtImmutablePointF with zero x and y values is correctly converted to a FloatArray.
        val point =  KtImmutablePointF(0f, 0f)
        val expectedArray = floatArrayOf(0f, 0f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun mixedPositiveAndNegativeValues() {
        // Verify correct conversion when  KtImmutablePointF has one positive and one negative value.
        val point =  KtImmutablePointF(10.5f, -20.5f)
        val expectedArray = floatArrayOf(10.5f, -20.5f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun maximumFloatValues() {
        // Test the conversion with Float.MAX_VALUE for both x and y to check for any overflow or precision issues.
        val point =  KtImmutablePointF(Float.MAX_VALUE, Float.MAX_VALUE)
        val expectedArray = floatArrayOf(Float.MAX_VALUE, Float.MAX_VALUE)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun minimumFloatValues() {
        // Test the conversion with Float.MIN_VALUE for both x and y to check for any underflow or precision issues.
        val point =  KtImmutablePointF(Float.MIN_VALUE, Float.MIN_VALUE)
        val expectedArray = floatArrayOf(Float.MIN_VALUE, Float.MIN_VALUE)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun positiveInfinityValues() {
        // Test the function's behavior when x and y are Float.POSITIVE_INFINITY.
        val point =  KtImmutablePointF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        val expectedArray = floatArrayOf(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun negativeInfinityValues() {
        // Test the function's behavior when x and y are Float.NEGATIVE_INFINITY.
        val point =  KtImmutablePointF(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        val expectedArray = floatArrayOf(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun nanValues() {
        // Test the function's behavior when x and y are Float.NaN (Not a Number).
        val point =  KtImmutablePointF(Float.NaN, Float.NaN)
        val expectedArray = floatArrayOf(Float.NaN, Float.NaN)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun companionObjectZeroConversion() {
        // Verify that the predefined  KtImmutablePointF.ZERO object correctly converts to a float array of [0f, 0f].
        val point =  KtImmutablePointF.ZERO
        val expectedArray = floatArrayOf(0f, 0f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun offset() {
        val point =  KtImmutablePointF(10.5f, 20.5f)
        val expected =  KtImmutablePointF(15.5f, 30.5f)
        assertThat(point.offset(5.0f, 10.0f)).isEqualTo(expected)
    }

    @Test
    fun negate() {
        val point =  KtImmutablePointF(10.5f, 20.5f)
        val expected =  KtImmutablePointF(-10.5f, -20.5f)
        assertThat(point.negate()).isEqualTo(expected)
    }

    @Test
    fun length() {
        val point =  KtImmutablePointF(3f, 4f)
        assertThat(point.length()).isEqualTo(5f)
    }

    @Test
    fun toMutable() {
        val point =  KtImmutablePointF(3f, 4f).toMutable()
        assertThat(point).isInstanceOf( KtPointF::class)
        assertThat(point.x).isEqualTo(3f)
        assertThat(point.y).isEqualTo(4f)
    }
}
