package io.legere.geokt
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import kotlin.test.Test

class KtPointFTest {
    @Test
    fun positiveFloatValuesConversion() {
        // Verify that a PdfPointF with positive x and y float values is correctly converted to a FloatArray.
        val point = KtPointF(10.5f, 20.5f)
        val expectedArray = floatArrayOf(10.5f, 20.5f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun negativeFloatValuesConversion() {
        // Verify that a PdfPointF with negative x and y float values is correctly converted to a FloatArray.
        val point = KtPointF(-10.5f, -20.5f)
        val expectedArray = floatArrayOf(-10.5f, -20.5f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun zeroFloatValuesConversion() {
        // Verify that a KtPointF with zero x and y values is correctly converted to a FloatArray.
        val point = KtPointF(0f, 0f)
        val expectedArray = floatArrayOf(0f, 0f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun mixedPositiveAndNegativeValues() {
        // Verify correct conversion when KtPointF has one positive and one negative value.
        val point = KtPointF(10.5f, -20.5f)
        val expectedArray = floatArrayOf(10.5f, -20.5f)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun maximumFloatValues() {
        // Test the conversion with Float.MAX_VALUE for both x and y to check for any overflow or precision issues.
        val point = KtPointF(Float.MAX_VALUE, Float.MAX_VALUE)
        val expectedArray = floatArrayOf(Float.MAX_VALUE, Float.MAX_VALUE)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun minimumFloatValues() {
        // Test the conversion with Float.MIN_VALUE for both x and y to check for any underflow or precision issues.
        val point = KtPointF(Float.MIN_VALUE, Float.MIN_VALUE)
        val expectedArray = floatArrayOf(Float.MIN_VALUE, Float.MIN_VALUE)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun positiveInfinityValues() {
        // Test the function's behavior when x and y are Float.POSITIVE_INFINITY.
        val point = KtPointF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        val expectedArray = floatArrayOf(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun negativeInfinityValues() {
        // Test the function's behavior when x and y are Float.NEGATIVE_INFINITY.
        val point = KtPointF(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        val expectedArray = floatArrayOf(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun nanValues() {
        // Test the function's behavior when x and y are Float.NaN (Not a Number).
        val point = KtPointF(Float.NaN, Float.NaN)
        val expectedArray = floatArrayOf(Float.NaN, Float.NaN)
        assertThat(point.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun offset() {
        val point = KtPointF(10.5f, 20.5f)
        val expected = KtPointF(15.5f, 30.5f)
        point.offset(5.0f, 10.0f)
        assertThat(point).isEqualTo(expected)
    }

    @Test
    fun negate() {
        val point = KtPointF(10.5f, 20.5f)
        val expected = KtPointF(-10.5f, -20.5f)
        point.negate()
        assertThat(point).isEqualTo(expected)
    }

    @Test
    fun length() {
        val point = KtPointF(3f, 4f)
        assertThat(point.length()).isEqualTo(5f)
    }

    @Test
    fun toImmutable() {
        val point = KtPointF(3f, 4f).toImmutable()
        assertThat(point).isInstanceOf(KtImmutablePointF::class)
        assertThat(point.x).isEqualTo(3f)
        assertThat(point.y).isEqualTo(4f)
    }

    @Test
    fun setWithFloatPointValues() {
        val point = KtPointF()
        val src = KtImmutablePointF(10.5f, 20.5f)
        point.set(src)
        assertThat(point.x).isEqualTo(10.5f)
        assertThat(point.y).isEqualTo(20.5f)
    }

    @Test
    fun setWithFloatValues() {
        val point = KtPointF()
        point.set(10.5f, 20.5f)
        assertThat(point.x).isEqualTo(10.5f)
        assertThat(point.y).isEqualTo(20.5f)
    }
}
