package io.legere.geokt
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotSameInstanceAs
import kotlin.test.Test

class KtPointTest {
    @Test
    fun basicFunctionalityWithPositiveIntegers() {
        // Verify that the method correctly converts a KtPoint with positive x and y
        // coordinates into an IntArray of size 2, where the first element is x and the second is y.
        val point = KtPoint(10, 20)
        val expectedArray = intArrayOf(10, 20)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun functionalityWithNegativeIntegers() {
        // Verify that the method correctly handles negative integer values for both x and y coordinates,
        // preserving their signs in the resulting IntArray.
        val point = KtPoint(-10, -20)
        val expectedArray = intArrayOf(-10, -20)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun functionalityWithMixedPositiveAndNegativeIntegers() {
        // Ensure the method works correctly when one coordinate is positive and the other is negative, and vice-versa.
        val point = KtPoint(-10, 20)
        val expectedArray = intArrayOf(-10, 20)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun testingWithIntMaxValue() {
        // Test the method with a KtPoint where both x and y are Int.MAX_VALUE to ensure it handles
        // the maximum integer limit without overflow or issues.
        val point = KtPoint(Int.MAX_VALUE, Int.MAX_VALUE)
        val expectedArray = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun testingWithIntMinValue() {
        // Test the method with a KtPoint where both x and y are Int.MIN_VALUE to ensure it
        // handles the minimum integer limit correctly.
        val point = KtPoint(Int.MIN_VALUE, Int.MIN_VALUE)
        val expectedArray = intArrayOf(Int.MIN_VALUE, Int.MIN_VALUE)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun testingWithMixedIntMaxValueAndIntMinValue() {
        // Verify correct behavior when x is Int.MAX_VALUE and y is Int.MIN_VALUE, and the reverse case.
        val point1 = KtPoint(Int.MAX_VALUE, Int.MIN_VALUE)
        val point2 = KtPoint(Int.MIN_VALUE, Int.MAX_VALUE)
        val expectedArray1 = intArrayOf(Int.MAX_VALUE, Int.MIN_VALUE)
        val expectedArray2 = intArrayOf(Int.MIN_VALUE, Int.MAX_VALUE)
        assertThat(point1.toIntArray()).isEqualTo(expectedArray1)
        assertThat(point2.toIntArray()).isEqualTo(expectedArray2)
    }

    @Test
    fun returnArrayTypeAndSizeValidation() {
        // Confirm that the returned object is specifically an IntArray and that its size is always
        // 2, regardless of the input coordinate values.
        val point = KtPoint(10, 20)
        val result = point.toIntArray()
        assertThat(result).isInstanceOf(IntArray::class)
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun immutabilityOfTheOriginalObject() {
        // Verify that invoking toIntArray() does not mutate the state of the original KtPoint object.
        val originalPoint = KtPoint(10, 20)
        originalPoint.toIntArray()
        assertThat(originalPoint.x).isEqualTo(10)
        assertThat(originalPoint.y).isEqualTo(20)
    }

    @Test
    fun arrayInstanceUniqueness() {
        // Ensure that multiple calls to toIntArray() on the same KtPoint instance return different
        // IntArray instances, proving a new array is created each time.
        val point = KtPoint(10, 20)
        val result1 = point.toIntArray()
        val result2 = point.toIntArray()
        assertThat(result1).isNotSameInstanceAs(result2)
    }

    @Test
    fun offset() {
        val point = KtPoint(10, 20)
        val expected = KtPoint(15, 30)
        point.offset(5, 10)
        assertThat(point).isEqualTo(expected)
    }

    @Test
    fun negate() {
        val point = KtPoint(10, 20)
        val expected = KtPoint(-10, -20)
        point.negate()
        assertThat(point).isEqualTo(expected)
    }

    @Test
    fun negate2() {
        val point = KtPoint(-10, -20)
        val expected = KtPoint(10, 20)
        point.negate()
        assertThat(point).isEqualTo(expected)
    }

    @Test
    fun toImmutable() {
        val point = KtPoint(3, 4).toImmutable()
        assertThat(point).isInstanceOf(KtImmutablePoint::class)
        assertThat(point.x).isEqualTo(3)
        assertThat(point.y).isEqualTo(4)
    }

    @Test
    fun setWithIntPointValues() {
        val point = KtPoint()
        val src = KtImmutablePoint(10, 20)
        point.set(src)
        assertThat(point.x).isEqualTo(10)
        assertThat(point.y).isEqualTo(20)
    }

    @Test
    fun setWithFloatValues() {
        val point = KtPoint()
        point.set(10, 20)
        assertThat(point.x).isEqualTo(10)
        assertThat(point.y).isEqualTo(20)
    }

    @Test
    fun length() {
        val point = KtPoint(3, 4)
        assertThat(point.length()).isEqualTo(5)
    }
}
