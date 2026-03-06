package io.legere.geokt
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import kotlin.test.Test

class KtImmutableRectTest {
    @Test
    fun defaultConstructor() {
        val rect = KtImmutableRect()
        assertThat(rect.isEmpty).isTrue()
    }

    @Test
    fun constructorWithFloatRectValues() {
        val input = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val rect = KtImmutableRect(input)
        assertThat(rect.left).isEqualTo(10)
        assertThat(rect.top).isEqualTo(20)
        assertThat(rect.right).isEqualTo(30)
        assertThat(rect.bottom).isEqualTo(40)
    }

    @Test
    fun constructorWithIntRectValues() {
        val input = KtImmutableRect(10, 20, 30, 40)
        val rect = KtImmutableRect(input)
        assertThat(rect.left).isEqualTo(10)
        assertThat(rect.top).isEqualTo(20)
        assertThat(rect.right).isEqualTo(30)
        assertThat(rect.bottom).isEqualTo(40)
    }

    @Test
    fun setEmptySetsToDefaultValues() {
        val input = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val rect = KtImmutableRect(input).setEmpty()
        assertThat(rect.isEmpty).isTrue()
    }

    @Test
    fun toIntArrayWithPositiveIntegers() {
        // Check if the function correctly converts a KtImmutableRect with all positive integers to an IntArray.
        val rect = KtImmutableRect(10, 20, 30, 40)
        val expectedArray = intArrayOf(10, 20, 30, 40)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithNegativeIntegers() {
        // Check if the function correctly converts a KtImmutableRect with all negative integers to an IntArray.
        val rect = KtImmutableRect(-10, -20, -30, -40)
        val expectedArray = intArrayOf(-10, -20, -30, -40)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithMixedIntegers() {
        // Check if the function correctly converts a KtImmutableRect with a mix of positive and negative
        // integers to an IntArray.
        val rect = KtImmutableRect(-10, 20, 30, -40)
        val expectedArray = intArrayOf(-10, 20, 30, -40)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithZeroValues() {
        // Check if the function correctly converts a KtImmutableRect with all zero values (KtImmutableRect.EMPTY) to an IntArray.
        val rect = KtImmutableRect(0, 0, 0, 0)
        val expectedArray = intArrayOf(0, 0, 0, 0)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithIntMAX_VALUE() {
        // Check if the function correctly converts a KtImmutableRect with Int.MAX_VALUE for all properties to an IntArray.
        val rect = KtImmutableRect(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        val expectedArray = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithIntMIN_VALUE() {
        // Check if the function correctly converts a KtImmutableRect with Int.MIN_VALUE for all properties to an IntArray.
        val rect = KtImmutableRect(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        val expectedArray = intArrayOf(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun widthWithPositiveIntegers() {
        // Check if the width is calculated correctly for a standard case where right > left.
        val rect = KtImmutableRect(10, 20, 30, 40)
        assertThat(rect.width).isEqualTo(20)
        assertThat(rect.width()).isEqualTo(20)
    }

    @Test
    fun widthResultingInZero() {
        // Check if the width is calculated as 0 when right equals left.
        val rect = KtImmutableRect(10, 20, 10, 40)
        assertThat(rect.width).isEqualTo(0)
        assertThat(rect.width()).isEqualTo(0)
    }

    @Test
    fun widthWithNegativeCoordinates() {
        // Check if the width is calculated correctly when both right and left are negative.
        val rect = KtImmutableRect(-10, 20, -30, 40)
        assertThat(rect.width).isEqualTo(-20)
        assertThat(rect.width()).isEqualTo(-20)
    }

    @Test
    fun widthWithInvertedCoordinates() {
        // Check if the width is calculated as a negative value when left > right.
        val rect = KtImmutableRect(30, 20, 10, 40)
        assertThat(rect.width).isEqualTo(-20)
        assertThat(rect.width()).isEqualTo(-20)
    }

    @Test
    fun widthIntegerOverflowCheck() {
        // Test for integer overflow when calculating width, e.g., right = Int.MAX_VALUE and left = Int.MIN_VALUE. [16]
        val rect = KtImmutableRect(Int.MIN_VALUE, 20, Int.MAX_VALUE, 40)
        assertThat(rect.width).isEqualTo(-1)
        assertThat(rect.width()).isEqualTo(-1)
    }

    @Test
    fun widthIntegerUnderflowCheck() {
        // Test for integer underflow when calculating width, e.g., right = Int.MIN_VALUE and left = Int.MAX_VALUE.
        val rect = KtImmutableRect(Int.MAX_VALUE, 20, Int.MIN_VALUE, 40)
        assertThat(rect.width).isEqualTo(1)
        assertThat(rect.width()).isEqualTo(1)
    }

    @Test
    fun heightWithPositiveIntegers() {
        // Check if the height is calculated correctly for a standard case where bottom > top.
        val rect = KtImmutableRect(10, 20, 30, 40)
        assertThat(rect.height).isEqualTo(20)
        assertThat(rect.height()).isEqualTo(20)
    }

    @Test
    fun heightResultingInZero() {
        // Check if the height is calculated as 0 when bottom equals top.
        val rect = KtImmutableRect(10, 20, 30, 20)
        assertThat(rect.height).isEqualTo(0)
        assertThat(rect.height()).isEqualTo(0)
    }

    @Test
    fun heightWithNegativeCoordinates() {
        // Check if the height is calculated correctly when both bottom and top are negative.
        val rect = KtImmutableRect(10, -20, 30, -40)
        assertThat(rect.height).isEqualTo(-20)
        assertThat(rect.height()).isEqualTo(-20)
    }

    @Test
    fun heightWithInvertedCoordinates() {
        // Check if the height is calculated as a negative value when top > bottom.
        val rect = KtImmutableRect(10, 40, 30, 20)
        assertThat(rect.height).isEqualTo(-20)
        assertThat(rect.height()).isEqualTo(-20)
    }

    @Test
    fun heightIntegerOverflowCheck() {
        // Test for integer overflow when calculating height, e.g., bottom = Int.MAX_VALUE and top = Int.MIN_VALUE. [16]
        val rect = KtImmutableRect(10, Int.MIN_VALUE, 30, Int.MAX_VALUE)
        assertThat(rect.height).isEqualTo(-1)
        assertThat(rect.height()).isEqualTo(-1)
    }

    @Test
    fun heightIntegerUnderflowCheck() {
        // Test for integer underflow when calculating height, e.g., bottom = Int.MIN_VALUE and top = Int.MAX_VALUE.
        val rect = KtImmutableRect(10, Int.MAX_VALUE, 30, Int.MIN_VALUE)
        assertThat(rect.height).isEqualTo(1)
        assertThat(rect.height()).isEqualTo(1)
    }

    @Test
    fun EMPTYObjectWidthCalculation() {
        // Verify that the width of the KtImmutableRect.EMPTY companion object is 0.
        val rect = KtImmutableRect.EMPTY
        assertThat(rect.width).isEqualTo(0)
        assertThat(rect.width()).isEqualTo(0)
    }

    @Test
    fun EMPTYObjectHeightCalculation() {
        // Verify that the height of the KtImmutableRect.EMPTY companion object is 0.
        val rect = KtImmutableRect.EMPTY
        assertThat(rect.height).isEqualTo(0)
        assertThat(rect.height()).isEqualTo(0)
    }

    @Test
    fun offset() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(5, 5, 15, 15)
        val result = rect.offset(5, 5)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun offsetTo() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(5, 5, 15, 15)
        val result = rect.offsetTo(5, 5)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun sort() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(0, 0, 10, 10)
        val result = rect.sort()
        assertThat(result).isEqualTo(expected)

        val rect2 = KtImmutableRect(10, 0, 0, 10)
        val expected2 = KtImmutableRect(0, 0, 10, 10)
        val result2 = rect2.sort()
        assertThat(result2).isEqualTo(expected2)
    }

    @Test
    fun inset() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(2, 2, 8, 8)
        val result = rect.inset(2, 2)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun inset00() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(0, 0, 10, 10)
        val result = rect.inset(0, 0)
        assertThat(result).isEqualTo(expected)
        val result2 = result.inset(0, 10)
        assertThat(result2).isEqualTo(KtImmutableRect(left = 0, top = 10, right = 10, bottom = 0))
        val result3 = result2.inset(10, 0)
        assertThat(result3).isEqualTo(KtImmutableRect(left = 10, top = 10, right = 0, bottom = 0))
    }

    @Test
    fun inset2() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(2, 2, 8, 8)
        val result = rect.inset(2, 2, 2, 2)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun intersect() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        val expected = KtImmutableRect(5, 5, 10, 10)
        val result = rect1.intersect(rect2)
        assertThat(result).isEqualTo(expected)

        val rect1Unprocessed = KtImmutableRect(0, 0, 10, 10)
        val rect3 = KtImmutableRect(0, 0, 10, 10)
        val result2 = rect3.intersect(rect1Unprocessed)
        assertThat(result2).isEqualTo(rect1Unprocessed)
        val result3 = rect1Unprocessed.intersect(rect3)
        assertThat(result3).isEqualTo(rect1Unprocessed)
    }

    @Test
    fun intersectAEmpty() {
        val rect1 = KtImmutableRect()
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        val expected = KtImmutableRect()
        val result = rect1.intersect(rect2)
        assertThat(result).isEqualTo(expected)
        val result2 = rect2.intersect(rect1)
        assertThat(result2).isEqualTo(KtImmutableRect(5, 5, 15, 15))
    }

    @Test
    fun intersect2() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(5, 5, 15, 15)
        val result = rect1.intersect(5, 5, 15, 15)
        assertThat(result).isEqualTo(expected)

        val rect1Unprocessed = KtImmutableRect(0, 0, 10, 10)
        val rect3 = KtImmutableRect(0, 0, 10, 10)
        val result2 = rect3.intersect(0, 0, 10, 10)
        assertThat(result2).isEqualTo(rect1Unprocessed)
        val result3 = rect1Unprocessed.intersect(0, 0, 10, 10)
        assertThat(result3).isEqualTo(rect1Unprocessed)
    }

    @Test
    fun intersect2Empty() {
        val rect1 = KtImmutableRect()
        val result = rect1.intersect(5, 5, 15, 15)
        assertThat(result).isEqualTo(KtImmutableRect())
    }

    @Test
    fun union() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        val expected = KtImmutableRect(0, 0, 15, 15)
        assertThat(rect1.union(rect2)).isEqualTo(expected)
        assertThat(rect1.union(5, 5, 15, 15)).isEqualTo(expected)
        assertThat(rect1.union(15, 15)).isEqualTo(expected)

        val emptyRect = KtImmutableRect.EMPTY
        assertThat(rect1.union(emptyRect)).isEqualTo(rect1)
        assertThat(rect1.union(0, 0, 0, 0)).isEqualTo(rect1)
        assertThat(emptyRect.union(rect1)).isEqualTo(rect1)
        assertThat(emptyRect.union(0, 0, 10, 10)).isEqualTo(rect1)
        assertThat(emptyRect.union(KtRect(0, 0, 10, 10))).isEqualTo(KtImmutableRect(0, 0, 10, 10))
    }

    @Test
    fun unionEmpty() {
        val rect1 = KtImmutableRect()
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        val result = rect1.union(rect2)
        assertThat(result).isEqualTo(rect2)

        val emptyRect = KtImmutableRect()
        val result2 = rect2.union(emptyRect)
        assertThat(result2).isEqualTo(result)
        val result3 = KtImmutableRect().union(rect1)
        assertThat(result3).isEqualTo(rect1)
    }

    @Test
    fun union2() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(0, 0, 15, 15)
        val result = rect1.union(5, 5, 15, 15)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun union2Empty() {
        val rect1 = KtImmutableRect()
        val other = KtImmutableRect(5, 5, 15, 15)
        val result = rect1.union(5, 5, 15, 15)
        assertThat(result).isEqualTo(other)
    }

    @Test
    fun union3() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val expected = KtImmutableRect(0, 0, 15, 15)
        val result = rect1.union(15, 15)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun union3Empty() {
        val rect1 = KtImmutableRect()
        val result = rect1.union(15, 15)
        assertThat(result).isEqualTo(rect1)
    }

    @Test
    fun contains() {
        val rect = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect.contains(5, 5)).isTrue()
        assertThat(rect.contains(0, 0)).isTrue() // Inclusive left/top
        assertThat(rect.contains(10, 10)).isFalse() // Exclusive right/bottom
        assertThat(rect.contains(11, 5)).isFalse()
        assertThat(rect.contains(-5, -5)).isFalse()
        assertThat(rect.contains(5, -5)).isFalse()
        assertThat(rect.contains(15, 15)).isFalse()

        val insideRect = KtImmutableRect(2, 2, 8, 8)
        assertThat(rect.contains(insideRect)).isTrue()

        val overlappingRect = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect.contains(overlappingRect)).isFalse()
        val insideRectFloat = KtImmutableRectF(2.0f, 2.0f, 8.0f, 8.0f)
        assertThat(rect.contains(insideRectFloat)).isTrue()

        val overlappingRectFloat = KtImmutableRectF(5.0f, 5.0f, 15.0f, 15.0f)
        assertThat(rect.contains(overlappingRectFloat)).isFalse()
    }

    @Test
    fun containsEmpty() {
        val rect = KtImmutableRect()
        assertThat(rect.contains(5, 5)).isFalse()
        assertThat(rect.contains(0, 0)).isFalse() // Inclusive left/top
        assertThat(rect.contains(10, 10)).isFalse() // Exclusive right/bottom
        assertThat(rect.contains(11, 5)).isFalse()

        val insideRect = KtImmutableRect(2, 2, 8, 8)
        assertThat(rect.contains(insideRect)).isFalse()

        val overlappingRect = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect.contains(overlappingRect)).isFalse()
        val insideRectFloat = KtImmutableRectF(2.0f, 2.0f, 8.0f, 8.0f)
        assertThat(rect.contains(insideRectFloat)).isFalse()

        val overlappingRectFloat = KtImmutableRectF(5.0f, 5.0f, 15.0f, 15.0f)
        assertThat(rect.contains(overlappingRectFloat)).isFalse()
    }

    @Test
    fun contains2() {
        val rect = KtImmutableRect(0, 0, 10, 10)

        assertThat(rect.contains(2, 2, 8, 8)).isTrue()

        assertThat(rect.contains(5, 5, 15, 15)).isFalse()
    }

    @Test
    fun contains2Empty() {
        val rect = KtImmutableRect()

        assertThat(rect.contains(2, 2, 8, 8)).isFalse()

        assertThat(rect.contains(5, 5, 15, 15)).isFalse()
    }

    @Test
    fun intersects() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect1.intersects(rect2)).isTrue()

        val rect3 = KtImmutableRect(20, 20, 30, 30)
        assertThat(rect1.intersects(rect3)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRect(10, 0, 20, 10)
        assertThat(rect1.intersects(rect4)).isTrue()
    }

    @Test
    fun intersectsEachBound() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        val leftOut = KtImmutableRect(5, 5, 10, 10).offset(0, -10)
        assertThat(rect1.intersects(leftOut)).isFalse()
        val topOut = KtImmutableRect(5, 5, 10, 10).offset(-10, 0)
        assertThat(rect1.intersects(topOut)).isFalse()
        val rightOUt = KtImmutableRect(5, 5, 10, 10).offset(0, 10)
        assertThat(rect1.intersects(rightOUt)).isFalse()
        val bottomOut = KtImmutableRect(5, 5, 10, 10).offset(10, 0)
        assertThat(rect1.intersects(bottomOut)).isFalse()
    }

    @Test
    fun intersectsEachBound2() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        assertThat(rect1.intersects(5, -5, 10, 0)).isFalse()
        assertThat(rect1.intersects(-5, 5, 0, 10)).isFalse()
        assertThat(rect1.intersects(5, 15, 10, 20)).isFalse()
        assertThat(rect1.intersects(15, 5, 20, 10)).isFalse()
    }

    @Test
    fun intersectsEachBound23() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        assertThat(rect1.intersects(5, -5, 10, 0)).isFalse()
        assertThat(rect1.intersects(-5, 5, 0, 10)).isFalse()
        assertThat(rect1.intersects(5, 15, 10, 20)).isFalse()
        assertThat(rect1.intersects(15, 5, 20, 10)).isFalse()
    }

    @Test
    fun intersectsEmpty() {
        val rect1 = KtImmutableRect()
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect1.intersects(rect2)).isFalse()
        assertThat(rect1.intersects(5, 5, 15, 15)).isFalse()

        val rect5 = KtImmutableRect(0, 0, 10, 10)
        val rect3 = KtImmutableRect()
        assertThat(rect5.intersects(rect3)).isFalse()
    }

    @Test
    fun intersects2() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect1.intersects(5, 5, 15, 15)).isTrue()

        assertThat(rect1.intersects(20, 20, 30, 30)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRect(10, 0, 20, 10)
        assertThat(rect1.intersects(rect4)).isTrue()
    }

    @Test
    fun containsX() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect1.contains(rect2)).isFalse()

        val rect3 = KtImmutableRect(20, 20, 30, 30)
        assertThat(rect1.contains(rect3)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRect(10, 0, 20, 10)
        assertThat(rect1.contains(rect4)).isFalse()
    }

    @Test
    fun containsEachBound() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        val leftOut = KtImmutableRect(5, 5, 10, 10).offset(0, -10)
        assertThat(rect1.contains(leftOut)).isFalse()
        val topOut = KtImmutableRect(5, 5, 10, 10).offset(-10, 0)
        assertThat(rect1.contains(topOut)).isFalse()
        val rightOUt = KtImmutableRect(5, 5, 10, 10).offset(0, 10)
        assertThat(rect1.contains(rightOUt)).isFalse()
        val bottomOut = KtImmutableRect(5, 5, 10, 10).offset(10, 0)
        assertThat(rect1.contains(bottomOut)).isFalse()
    }

    @Test
    fun containsEachBoundFloat() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        val leftOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(0f, -10f)
        assertThat(rect1.contains(leftOut)).isFalse()
        val topOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(-10f, 0f)
        assertThat(rect1.contains(topOut)).isFalse()
        val rightOUt = KtImmutableRectF(5f, 5f, 10f, 10f).offset(0f, 10f)
        assertThat(rect1.contains(rightOUt)).isFalse()
        val bottomOut = KtImmutableRectF(5f, 5f, 10f, 10f).offset(10f, 0f)
        assertThat(rect1.contains(bottomOut)).isFalse()
        val empty = KtImmutableRectF()
        assertThat(rect1.contains(empty)).isFalse()
    }

    @Test
    fun containsEachBound2() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        assertThat(rect1.contains(5, -5, 10, 0)).isFalse()
        assertThat(rect1.contains(-5, 5, 0, 10)).isFalse()
        assertThat(rect1.contains(5, 15, 10, 20)).isFalse()
        assertThat(rect1.contains(15, 5, 20, 10)).isFalse()
    }

    @Test
    fun containsEachBound22() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        assertThat(rect1.contains(-5, -5)).isFalse()
        assertThat(rect1.contains(5, -5)).isFalse()
        assertThat(rect1.contains(50, 8)).isFalse()
        assertThat(rect1.contains(8, 50)).isFalse()
    }

    @Test
    fun containsEachBound23() {
        val rect1 = KtImmutableRect(5, 5, 10, 10)
        assertThat(rect1.contains(5, -5, 10, 0)).isFalse()
        assertThat(rect1.contains(-5, 5, 0, 10)).isFalse()
        assertThat(rect1.contains(5, 15, 10, 20)).isFalse()
        assertThat(rect1.contains(15, 5, 20, 10)).isFalse()
    }

    @Test
    fun containsEmptyX() {
        val rect1 = KtImmutableRect()
        val rect2 = KtImmutableRect(5, 5, 15, 15)
        assertThat(rect1.contains(rect2)).isFalse()
        assertThat(rect1.contains(5, 5, 15, 15)).isFalse()

        val rect5 = KtImmutableRect(0, 0, 10, 10)
        val rect3 = KtImmutableRect()
        assertThat(rect5.contains(rect3)).isFalse()
    }

    @Test
    fun contains2X() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect1.contains(5, 5, 15, 15)).isFalse()

        assertThat(rect1.contains(20, 20, 30, 30)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtImmutableRect(10, 0, 20, 10)
        assertThat(rect1.contains(rect4)).isFalse()
    }

    @Test
    fun width() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect1.width).isEqualTo(10)

        val rect2 = KtImmutableRect(10, 0, 20, 10)
        assertThat(rect2.width).isEqualTo(10)
    }

    @Test
    fun height() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect1.height).isEqualTo(10)

        val rect2 = KtImmutableRect(10, 0, 20, 20)
        assertThat(rect2.height).isEqualTo(20)
    }

    @Test
    fun center() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect1.centerX).isEqualTo(5)
        assertThat(rect1.centerX()).isEqualTo(5)
        assertThat(rect1.centerY).isEqualTo(5)
        assertThat(rect1.centerY()).isEqualTo(5)

        val rect2 = KtImmutableRect(10, 0, 20, 20)
        assertThat(rect2.centerX).isEqualTo(15)
        assertThat(rect2.centerX()).isEqualTo(15)
        assertThat(rect2.centerY).isEqualTo(10)
        assertThat(rect2.centerY()).isEqualTo(10)

        val rect3 = KtImmutableRect(0, 0, 15, 15)
        assertThat(rect3.exactCenterX).isEqualTo(7.5f)
        assertThat(rect3.exactCenterY).isEqualTo(7.5f)
    }

    @Test
    fun isEmpty() {
        val rect1 = KtImmutableRect(0, 0, 10, 10)
        assertThat(rect1.isEmpty).isFalse()
        assertThat(rect1.isEmpty()).isFalse()

        val rect2 = KtImmutableRect.EMPTY
        assertThat(rect2.isEmpty).isTrue()
        assertThat(rect2.isEmpty()).isTrue()

        assertThat(KtImmutableRect(0, 0, -5, -5).isEmpty).isTrue()
        assertThat(KtImmutableRect(0, 0, 1, -5).isEmpty).isTrue()
    }

    @Test
    fun toMutable() {
        val rect = KtImmutableRect(1, 2, 3, 4)
        val mutable = rect.toMutable()
        assertThat(mutable).isInstanceOf(KtRect::class)
        assertThat(mutable.left).isEqualTo(1)
        assertThat(mutable.top).isEqualTo(2)
        assertThat(mutable.right).isEqualTo(3)
        assertThat(mutable.bottom).isEqualTo(4)
    }
}
