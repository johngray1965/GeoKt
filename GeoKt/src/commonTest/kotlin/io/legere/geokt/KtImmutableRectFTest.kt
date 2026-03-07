package io.legere.geokt

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import kotlin.test.Test

class KtImmutableRectFTest {
    @Test
    fun defaultConstructor() {
        val rect = KtImmutableRectF()
        assertThat(rect.isEmpty).isTrue()
    }

    @Test
    fun constructorWithFloatRectValues() {
        val input = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val rect = KtImmutableRectF(input)
        assertThat(rect.left).isEqualTo(10.5f)
        assertThat(rect.top).isEqualTo(20.5f)
        assertThat(rect.right).isEqualTo(30.5f)
        assertThat(rect.bottom).isEqualTo(40.5f)
    }

    @Test
    fun constructorWithIntRectValues() {
        val input = KtImmutableRect(10, 20, 30, 40)
        val rect = KtImmutableRectF(input)
        assertThat(rect.left).isEqualTo(10.0f)
        assertThat(rect.top).isEqualTo(20.0f)
        assertThat(rect.right).isEqualTo(30.0f)
        assertThat(rect.bottom).isEqualTo(40.0f)
    }

    @Test
    fun setEmptySetsToDefaultValues() {
        val input = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val rect = KtImmutableRectF(input).setEmpty()
        assertThat(rect.isEmpty).isTrue()
    }

    @Test
    fun toFloatArrayWithPositiveValues() {
        // Verify that toFloatArray correctly returns an array with positive float values.
        val rect = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val expectedArray = floatArrayOf(10.5f, 20.5f, 30.5f, 40.5f)
        assertThat(rect.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toFloatArrayWithNegativeValues() {
        // Verify that toFloatArray correctly returns an array with negative float values.
        val rect = KtImmutableRectF(-10.5f, -20.5f, -30.5f, -40.5f)
        val expectedArray = floatArrayOf(-10.5f, -20.5f, -30.5f, -40.5f)
        assertThat(rect.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toFloatArrayWithZeroValues() {
        // Test toFloatArray with all coordinates set to zero, including the EMPTY companion object.
        val rect = KtImmutableRectF.EMPTY
        val expectedArray = floatArrayOf(0f, 0f, 0f, 0f)
        assertThat(rect.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toFloatArrayWithMixedSignValues() {
        // Ensure toFloatArray functions correctly when coordinates have a mix of positive and negative values.
        val rect = KtImmutableRectF(-10.5f, 20.5f, 30.5f, -40.5f)
        val expectedArray = floatArrayOf(-10.5f, 20.5f, 30.5f, -40.5f)
        assertThat(rect.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toFloatArrayWithLargeMagnitudeValues() {
        // Test toFloatArray with very large positive and negative float values to check for precision issues.
        val rect = KtImmutableRectF(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
        val expectedArray = floatArrayOf(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
        assertThat(rect.toFloatArray()).isEqualTo(expectedArray)
    }

    @Test
    fun widthWithPositiveCoordinates() {
        // Calculate width for a standard rectangle where right is greater than left.
        val rect = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        assertThat(rect.width).isEqualTo(20.0f)
    }

    @Test
    fun widthWithAZeroWidthRectangle() {
        // Test width calculation when right and left coordinates are equal, expecting a result of 0.
        val rect = KtImmutableRectF(10.5f, 20.5f, 10.5f, 40.5f)
        assertThat(rect.width).isEqualTo(0.0f)
    }

    @Test
    fun widthResultingInANegativeValue() {
        // Check the width calculation for a rectangle where the left coordinate is greater than the right coordinate.
        val rect = KtImmutableRectF(30.5f, 20.5f, 10.5f, 40.5f)
        assertThat(rect.width).isEqualTo(-20.0f)
    }

    @Test
    fun widthWithNegativeCoordinates() {
        // Verify width calculation when both left and right coordinates are negative.
        val rect = KtImmutableRectF(-10.5f, 20.5f, -30.5f, 40.5f)
        assertThat(rect.width).isEqualTo(-20.0f)
    }

    @Test
    fun widthSpanningTheZeroAxis() {
        // Calculate width for a rectangle that crosses the y-axis (e.g., left is negative, right is positive).
        val rect = KtImmutableRectF(-10.5f, 20.5f, 10.5f, 40.5f)
        assertThat(rect.width).isEqualTo(21.0f)
    }

    @Test
    fun widthWithFloatingPointPrecision() {
        // Test width calculation with floating-point numbers that require high precision to ensure accuracy.
        val rect = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        assertThat(rect.width).isEqualTo(20.0f)
    }

    @Test
    fun heightWithPositiveCoordinates() {
        // Calculate height for a standard rectangle where bottom is greater than top.
        val rect = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        assertThat(rect.height).isEqualTo(20.0f)
    }

    @Test
    fun heightWithAZeroHeightRectangle() {
        // Test height calculation when bottom and top coordinates are equal, expecting a result of 0.
        val rect = KtImmutableRectF(10.5f, 20.5f, 30.5f, 20.5f)
        assertThat(rect.height).isEqualTo(0.0f)
    }

    @Test
    fun heightResultingInANegativeValue() {
        // Check the height calculation for a rectangle where the top coordinate is greater than
        // the bottom coordinate (inverted rectangle).
        val rect = KtImmutableRectF(10.5f, 40.5f, 30.5f, 20.5f)
        assertThat(rect.height).isEqualTo(-20.0f)
    }

    @Test
    fun heightWithNegativeCoordinates() {
        // Verify height calculation when both top and bottom coordinates are negative.
        val rect = KtImmutableRectF(10.5f, -20.5f, 30.5f, -40.5f)
        assertThat(rect.height).isEqualTo(-20.0f)
    }

    @Test
    fun heightSpanningTheZeroAxis() {
        // Calculate height for a rectangle that crosses the x-axis (e.g., top is negative, bottom is positive).
        val rect = KtImmutableRectF(10.5f, -20.5f, 30.5f, 40.5f)
        assertThat(rect.height).isEqualTo(61.0f)
    }

    @Test
    fun heightWithFloatingPointPrecision() {
        // Test height calculation with floating-point numbers that require high precision to ensure accuracy.
        val rect = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        assertThat(rect.height).isEqualTo(20.0f)
    }

    @Test
    fun offset() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(5f, 5f, 15f, 15f)
        val result = rect.offset(5f, 5f)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun offsetTo() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(5f, 5f, 15f, 15f)
        val result = rect.offsetTo(5f, 5f)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun sort() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(0f, 0f, 10f, 10f)
        val result = rect.sort()
        assertThat(result).isEqualTo(expected)

        val rect2 = KtImmutableRectF(10f, 0f, 0f, 10f)
        val expected2 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val result2 = rect2.sort()
        assertThat(result2).isEqualTo(expected2)
    }

    @Test
    fun inset() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(2f, 2f, 8f, 8f)
        val result = rect.inset(2f, 2f)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun inset00() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(0f, 0f, 10f, 10f)
        val result = rect.inset(0f, 0f)
        assertThat(result).isEqualTo(expected)
        val result2 = result.inset(0f, 10f)
        assertThat(result2).isEqualTo(KtImmutableRectF(left = 0f, top = 10f, right = 10f, bottom = 0f))
        val result3 = result2.inset(10f, 0f)
        assertThat(result3).isEqualTo(KtImmutableRectF(left = 10f, top = 10f, right = 0f, bottom = 0f))
    }

    @Test
    fun inset2() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(2f, 2f, 8f, 8f)
        val result = rect.inset(2f, 2f, 2f, 2f)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun intersect() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        val expected = KtImmutableRectF(5f, 5f, 10f, 10f)
        val result = rect1.intersect(rect2)
        assertThat(result).isEqualTo(expected)

        val rect1Unprocessed = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect3 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val result2 = rect3.intersect(rect1Unprocessed)
        assertThat(result2).isEqualTo(rect1Unprocessed)
        val result3 = rect1Unprocessed.intersect(rect3)
        assertThat(result3).isEqualTo(rect1Unprocessed)
    }

    @Test
    fun intersectAEmpty() {
        val rect1 = KtImmutableRectF()
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        val expected = KtImmutableRectF()
        val result = rect1.intersect(rect2)
        assertThat(result).isEqualTo(expected)
        val result2 = rect2.intersect(rect1)
        assertThat(result2).isEqualTo(KtImmutableRectF(5f, 5f, 15f, 15f))
    }

    @Test
    fun intersect2() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(5f, 5f, 15f, 15f)
        val result = rect1.intersect(5f, 5f, 15f, 15f)
        assertThat(result).isEqualTo(expected)

        val rect1Unprocessed = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect3 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val result2 = rect3.intersect(0f, 0f, 10f, 10f)
        assertThat(result2).isEqualTo(rect1Unprocessed)
        val result3 = rect1Unprocessed.intersect(0f, 0f, 10f, 10f)
        assertThat(result3).isEqualTo(rect1Unprocessed)
    }

    @Test
    fun intersect2Empty() {
        val rect1 = KtImmutableRectF()
        val result = rect1.intersect(5f, 5f, 15f, 15f)
        assertThat(result).isEqualTo(KtImmutableRectF())
    }

    @Test
    fun union() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        val expected = KtImmutableRectF(0f, 0f, 15f, 15f)
        assertThat(rect1.union(rect2)).isEqualTo(expected)

        val emptyRect = KtImmutableRectF.EMPTY
        assertThat(rect1.union(emptyRect)).isEqualTo(rect1)
        assertThat(emptyRect.union(rect1)).isEqualTo(rect1)
        assertThat(emptyRect.union(KtRectF(0f, 0f, 10f, 10f))).isEqualTo(KtImmutableRectF(0f, 0f, 10f, 10f))
    }

    @Test
    fun union22() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(0f, 0f, 15f, 15f)
        assertThat(rect1.union(5f, 5f, 15f, 15f)).isEqualTo(expected)
        assertThat(rect1.union(15f, 15f)).isEqualTo(expected)

        val emptyRect = KtImmutableRectF.EMPTY
        assertThat(rect1.union(0f, 0f, 0f, 0f)).isEqualTo(rect1)
        assertThat(emptyRect.union(0f, 0f, 10f, 10f)).isEqualTo(rect1)
    }

    @Test
    fun unionEmpty() {
        val rect1 = KtImmutableRectF()
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        val result = rect1.union(rect2)
        assertThat(result).isEqualTo(rect2)

        val emptyRect = KtImmutableRectF()
        val result2 = result.union(emptyRect)
        assertThat(result2).isEqualTo(result)
        val result3 = emptyRect.union(rect1)
        assertThat(result3).isEqualTo(rect1)
    }

    @Test
    fun union2() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(0f, 0f, 15f, 15f)
        val result = rect1.union(5f, 5f, 15f, 15f)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun union2Empty() {
        val rect1 = KtImmutableRectF()
        val other = KtImmutableRectF(5f, 5f, 15f, 15f)
        val result = rect1.union(5f, 5f, 15f, 15f)
        assertThat(result).isEqualTo(other)
    }

    @Test
    fun union3() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val expected = KtImmutableRectF(0f, 0f, 15f, 15f)
        val result = rect1.union(15f, 15f)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun union3Empty() {
        val rect1 = KtImmutableRectF()
        val result = rect1.union(15f, 15f)
        assertThat(result).isEqualTo(rect1)
    }

    @Test
    fun roundOut() {
        val rect = KtImmutableRectF(0.1f, 0.9f, 9.9f, 10.1f)
        val expected = KtImmutableRect(0, 0, 10, 11) // floor, floor, ceil, ceil

        // Assuming roundOut implementation:
        // left = floor(left), top = floor(top), right = ceil(right), bottom = ceil(bottom)
        // Wait, Android RectF.roundOut does exactly that.
        // My KtImmutableRectF does not have roundOut yet?
        // The user asked to add tests for it, implying it should be there or I should add it.
        // I checked TypeConversion.kt earlier, maybe it's there as extension?
        // Or maybe I missed adding it to KtImmutableRectF.kt?
        // Let's assume I need to add it to KtImmutableRectF.kt as well if it's missing.
        // But for now I'll write the test and see.
        // Wait, I cannot run tests to see if it fails.
        // I will add roundOut() to KtImmutableRectF.kt if I don't see it.
        // I read KtImmutableRectF.kt in previous turn, it did NOT have roundOut.
        // It has `toPdfRect` which does roundToInt.

        // I'll add the test assuming I will add the function.
        assertThat(rect.roundOut()).isEqualTo(expected)
    }

    @Test
    fun contains() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect.contains(5f, 5f)).isTrue()
        assertThat(rect.contains(0f, 0f)).isTrue() // Inclusive left/top
        assertThat(rect.contains(10f, 10f)).isFalse() // Exclusive right/bottom
        assertThat(rect.contains(11f, 5f)).isFalse()

        val insideRect = KtImmutableRectF(2f, 2f, 8f, 8f)
        assertThat(rect.contains(insideRect)).isTrue()

        val overlappingRect = KtImmutableRectF(5f, 5f, 15f, 15f)
        assertThat(rect.contains(overlappingRect)).isFalse()

        val insideRectInt = KtImmutableRect(2, 2, 8, 8)
        assertThat(rect.contains(insideRectInt)).isTrue()

        val overlappingRectInt = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect.contains(overlappingRectInt)).isFalse()
    }

    @Test
    fun containsEmpty() {
        val rect = KtImmutableRectF()
        assertThat(rect.contains(5f, 5f)).isFalse()
        assertThat(rect.contains(0f, 0f)).isFalse() // Inclusive left/top
        assertThat(rect.contains(10f, 10f)).isFalse() // Exclusive right/bottom
        assertThat(rect.contains(11f, 5f)).isFalse()

        val insideRect = KtImmutableRectF(2f, 2f, 8f, 8f)
        assertThat(rect.contains(insideRect)).isFalse()

        val overlappingRect = KtImmutableRectF(5f, 5f, 15f, 15f)
        assertThat(rect.contains(overlappingRect)).isFalse()
        val insideRectFloat = KtImmutableRectF(2.0f, 2.0f, 8.0f, 8.0f)
        assertThat(rect.contains(insideRectFloat)).isFalse()

        val overlappingRectFloat = KtImmutableRectF(5.0f, 5.0f, 15.0f, 15.0f)
        assertThat(rect.contains(overlappingRectFloat)).isFalse()
    }

    @Test
    fun contains2() {
        val rect = KtImmutableRectF(0f, 0f, 10f, 10f)

        assertThat(rect.contains(2f, 2f, 8f, 8f)).isTrue()

        assertThat(rect.contains(5f, 5f, 15f, 15f)).isFalse()
    }

    @Test
    fun contains2Empty() {
        val rect = KtImmutableRectF()

        assertThat(rect.contains(2f, 2f, 8f, 8f)).isFalse()

        assertThat(rect.contains(5f, 5f, 15f, 15f)).isFalse()
    }

    @Test
    fun intersects() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        assertThat(rect1.intersects(rect2)).isTrue()

        val rect3 = KtImmutableRectF(20f, 20f, 30f, 30f)
        assertThat(rect1.intersects(rect3)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRectF(10f, 0f, 20f, 10f)
        assertThat(rect1.intersects(rect4)).isTrue()
    }

    @Test
    fun intersectsEachBound() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        val leftOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(0f, -10f)
        assertThat(rect1.intersects(leftOut)).isFalse()
        val topOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(-10f, 0f)
        assertThat(rect1.intersects(topOut)).isFalse()
        val rightOUt = KtImmutableRectF(5f, 5f, 10f, 10f).offset(0f, 10f)
        assertThat(rect1.intersects(rightOUt)).isFalse()
        val bottomOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(10f, 0f)
        assertThat(rect1.intersects(bottomOut)).isFalse()
    }

    @Test
    fun intersectsEachBound2() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        assertThat(rect1.intersects(5f, -5f, 10f, 0f)).isFalse()
        assertThat(rect1.intersects(-5f, 5f, 0f, 10f)).isFalse()
        assertThat(rect1.intersects(5f, 15f, 10f, 20f)).isFalse()
        assertThat(rect1.intersects(15f, 5f, 20f, 10f)).isFalse()
    }

    @Test
    fun intersectsEmpty() {
        val rect1 = KtImmutableRectF()
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        assertThat(rect1.intersects(rect2)).isFalse()
        assertThat(rect1.intersects(5f, 5f, 15f, 15f)).isFalse()

        val rect5 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect3 = KtImmutableRectF()
        assertThat(rect5.intersects(rect3)).isFalse()
    }

    @Test
    fun intersects2() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect1.intersects(5f, 5f, 15f, 15f)).isTrue()

        assertThat(rect1.intersects(20f, 20f, 30f, 30f)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRectF(10f, 0f, 20f, 10f)
        assertThat(rect1.intersects(rect4)).isTrue()
    }

    @Test
    fun containsX() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        assertThat(rect1.contains(rect2)).isFalse()

        val rect3 = KtImmutableRectF(20f, 20f, 30f, 30f)
        assertThat(rect1.contains(rect3)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRectF(10f, 0f, 20f, 10f)
        assertThat(rect1.contains(rect4)).isFalse()
    }

    @Test
    fun containsEachBound() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        val leftOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(0f, -10f)
        assertThat(rect1.contains(leftOut)).isFalse()
        val topOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(-10f, 0f)
        assertThat(rect1.contains(topOut)).isFalse()
        val rightOUt = KtImmutableRectF(5f, 5f, 10f, 10f).offset(0f, 10f)
        assertThat(rect1.contains(rightOUt)).isFalse()
        val bottomOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(10f, 0f)
        assertThat(rect1.contains(bottomOut)).isFalse()
    }

    @Test
    fun containsEachBoundInt() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        val leftOut = KtImmutableRect(5, 5, 10, 10).offset(0, -10)
        assertThat(rect1.contains(leftOut)).isFalse()
        val topOut = KtImmutableRect(5, 5, 10, 10).offset(-10, 0)
        assertThat(rect1.contains(topOut)).isFalse()
        val rightOUt = KtImmutableRect(5, 5, 10, 10).offset(0, 10)
        assertThat(rect1.contains(rightOUt)).isFalse()
        val bottomOut = KtImmutableRect(5, 5, 10, 10).offset(10, 0)
        assertThat(rect1.contains(bottomOut)).isFalse()
        val empty = KtImmutableRect()
        assertThat(rect1.contains(empty)).isFalse()
        assertThat(KtImmutableRectF().contains(bottomOut)).isFalse()
    }

    @Test
    fun containsEachBound2() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        assertThat(rect1.contains(5f, -5f, 10f, 0f)).isFalse()
        assertThat(rect1.contains(-5f, 5f, 0f, 10f)).isFalse()
        assertThat(rect1.contains(5f, 15f, 10f, 20f)).isFalse()
        assertThat(rect1.contains(15f, 5f, 20f, 10f)).isFalse()
    }

    @Test
    fun containsEachBound22() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        assertThat(rect1.contains(-5f, -5f)).isFalse()
        assertThat(rect1.contains(5f, -5f)).isFalse()
        assertThat(rect1.contains(50f, 8f)).isFalse()
        assertThat(rect1.contains(8f, 50f)).isFalse()
        assertThat(KtImmutableRectF().contains(8f, 50f)).isFalse()
    }

    @Test
    fun containsEachBound23() {
        val rect1 = KtImmutableRectF(5f, 5f, 10f, 10f)
        assertThat(rect1.contains(5f, -5f, 10f, 0f)).isFalse()
        assertThat(rect1.contains(-5f, 5f, 0f, 10f)).isFalse()
        assertThat(rect1.contains(5f, 15f, 10f, 20f)).isFalse()
        assertThat(rect1.contains(15f, 5f, 20f, 10f)).isFalse()
    }

    @Test
    fun containsEmptyX() {
        val rect1 = KtImmutableRectF()
        val rect2 = KtImmutableRectF(5f, 5f, 15f, 15f)
        assertThat(rect1.contains(rect2)).isFalse()
        assertThat(rect1.contains(5f, 5f, 15f, 15f)).isFalse()

        val rect5 = KtImmutableRectF(0f, 0f, 10f, 10f)
        val rect3 = KtImmutableRectF()
        assertThat(rect5.contains(rect3)).isFalse()
    }

    @Test
    fun contains2X() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect1.contains(5f, 5f, 15f, 15f)).isFalse()

        assertThat(rect1.contains(20f, 20f, 30f, 30f)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRectF(10f, 0f, 20f, 10f)
        assertThat(rect1.contains(rect4)).isFalse()
    }

    @Test
    fun width() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect1.width).isEqualTo(10f)

        val rect2 = KtImmutableRectF(10f, 0f, 20f, 10f)
        assertThat(rect2.width).isEqualTo(10f)
    }

    @Test
    fun height() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect1.height).isEqualTo(10f)

        val rect2 = KtImmutableRectF(10f, 10f, 20f, 20f)
        assertThat(rect2.height).isEqualTo(10f)
    }

    @Test
    fun center() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect1.centerX).isEqualTo(5f)
        assertThat(rect1.centerY).isEqualTo(5f)

        val rect2 = KtImmutableRectF(10f, 10f, 20f, 20f)
        assertThat(rect2.centerX).isEqualTo(15f)
        assertThat(rect2.centerY).isEqualTo(15f)
    }

    @Test
    fun isEmpty() {
        val rect1 = KtImmutableRectF(0f, 0f, 10f, 10f)
        assertThat(rect1.isEmpty).isFalse()

        val rect2 = KtImmutableRectF.EMPTY
        assertThat(rect2.isEmpty).isTrue()

        assertThat(KtImmutableRectF(0f, 0f, -5f, -5f).isEmpty).isTrue()
        assertThat(KtImmutableRectF(0f, 0f, 1f, -5f).isEmpty).isTrue()
    }

    @Test
    fun toMutable() {
        val rect = KtImmutableRectF(1f, 2f, 3f, 4f)
        val mutable = rect.toMutable()
        assertThat(mutable).isInstanceOf(KtRectF::class)
        assertThat(mutable.left).isEqualTo(1f)
        assertThat(mutable.top).isEqualTo(2f)
        assertThat(mutable.right).isEqualTo(3f)
        assertThat(mutable.bottom).isEqualTo(4f)
    }
}
