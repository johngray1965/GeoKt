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

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotSameInstanceAs
import kotlin.test.Test

class KtImmutablePointTest {
    @Test
    fun basicFunctionalityWithPositiveIntegers() {
        // Verify that the method correctly converts a KtImmutablePoint with positive x and y coordinates into an IntArray of size 2,
        // where the first element is x and the second is y.
        val point = KtImmutablePoint(10, 20)
        val expectedArray = intArrayOf(10, 20)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun functionalityWithNegativeIntegers() {
        // Verify that the method correctly handles negative integer values for both x and y coordinates,
        // preserving their signs in the resulting IntArray.
        val point = KtImmutablePoint(-10, -20)
        val expectedArray = intArrayOf(-10, -20)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun functionalityWithZeroValues() {
        // Test the method using the predefined ZERO companion object (KtImmutablePoint(0, 0)) to ensure it produces an array containing two zeros.
        val point = KtImmutablePoint.ZERO
        val expectedArray = intArrayOf(0, 0)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun functionalityWithMixedPositiveAndNegativeIntegers() {
        // Ensure the method works correctly when one coordinate is positive and the other is negative, and vice-versa.
        val point = KtImmutablePoint(-10, 20)
        val expectedArray = intArrayOf(-10, 20)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun testingWithIntMaxValue() {
        // Test the method with a KtImmutablePoint where both x and y are Int.MAX_VALUE to ensure it handles
        // the maximum integer limit without overflow or issues.
        val point = KtImmutablePoint(Int.MAX_VALUE, Int.MAX_VALUE)
        val expectedArray = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun testingWithIntMinValue() {
        // Test the method with a KtImmutablePoint where both x and y are Int.MIN_VALUE to ensure it handles the minimum integer limit correctly.
        val point = KtImmutablePoint(Int.MIN_VALUE, Int.MIN_VALUE)
        val expectedArray = intArrayOf(Int.MIN_VALUE, Int.MIN_VALUE)
        assertThat(point.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun testingWithMixedIntMaxValueAndIntMinValue() {
        // Verify correct behavior when x is Int.MAX_VALUE and y is Int.MIN_VALUE, and the reverse case.
        val point1 = KtImmutablePoint(Int.MAX_VALUE, Int.MIN_VALUE)
        val point2 = KtImmutablePoint(Int.MIN_VALUE, Int.MAX_VALUE)
        val expectedArray1 = intArrayOf(Int.MAX_VALUE, Int.MIN_VALUE)
        val expectedArray2 = intArrayOf(Int.MIN_VALUE, Int.MAX_VALUE)
        assertThat(point1.toIntArray()).isEqualTo(expectedArray1)
        assertThat(point2.toIntArray()).isEqualTo(expectedArray2)
    }

    @Test
    fun returnArrayTypeAndSizeValidation() {
        // Confirm that the returned object is specifically an IntArray and that its size is always
        // 2, regardless of the input coordinate values.
        val point = KtImmutablePoint(10, 20)
        val result = point.toIntArray()
        assertThat(result).isInstanceOf(IntArray::class)
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun immutabilityOfTheOriginalObject() {
        // Verify that invoking toIntArray() does not mutate the state of the original KtImmutablePoint object.
        val originalPoint = KtImmutablePoint(10, 20)
        originalPoint.toIntArray()
        assertThat(originalPoint.x).isEqualTo(10)
        assertThat(originalPoint.y).isEqualTo(20)
    }

    @Test
    fun arrayInstanceUniqueness() {
        // Ensure that multiple calls to toIntArray() on the same KtImmutablePoint instance return different
        // IntArray instances, proving a new array is created each time.
        val point = KtImmutablePoint(10, 20)
        val result1 = point.toIntArray()
        val result2 = point.toIntArray()
        assertThat(result1).isNotSameInstanceAs(result2)
    }

    @Test
    fun offset() {
        val point = KtImmutablePoint(10, 20)
        val expected = KtImmutablePoint(15, 30)
        assertThat(point.offset(5, 10)).isEqualTo(expected)
    }

    @Test
    fun negate() {
        val point = KtImmutablePoint(10, 20)
        val expected = KtImmutablePoint(-10, -20)
        assertThat(point.negate()).isEqualTo(expected)
    }

    @Test
    fun length() {
        val point = KtImmutablePoint(3, 4)
        assertThat(point.length()).isEqualTo(5)
    }

    @Test
    fun toMutable() {
        val point = KtImmutablePoint(3, 4).toMutable()
        assertThat(point).isInstanceOf(KtPoint::class)
        assertThat(point.x).isEqualTo(3)
        assertThat(point.y).isEqualTo(4)
    }
}
