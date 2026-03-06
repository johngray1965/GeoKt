package io.legere.geokt
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import kotlin.test.Test

class KtRectTest {
    @Test
    fun defaultConstructor() {
        val rect = KtRect()
        assertThat(rect.isEmpty).isTrue()
    }

    @Test
    fun constructorWithFloatRectValues() {
        val input = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val rect = KtRect(input)
        assertThat(rect.left).isEqualTo(10)
        assertThat(rect.top).isEqualTo(20)
        assertThat(rect.right).isEqualTo(30)
        assertThat(rect.bottom).isEqualTo(40)
    }

    @Test
    fun constructorWithIntRectValues() {
        val input = KtImmutableRect(10, 20, 30, 40)
        val rect = KtRect(input)
        assertThat(rect.left).isEqualTo(10)
        assertThat(rect.top).isEqualTo(20)
        assertThat(rect.right).isEqualTo(30)
        assertThat(rect.bottom).isEqualTo(40)
    }

    @Test
    fun setEmptySetsToDefaultValues() {
        val input = KtImmutableRectF(10.5f, 20.5f, 30.5f, 40.5f)
        val rect = KtRect(input)
        rect.setEmpty()
        assertThat(rect.isEmpty).isTrue()
    }

    @Test
    fun set() {
        val rect = KtRect(0, 0, 10, 10)
        val rect2 = KtRect(20, 20, 50, 50)
        rect.set(rect2)
        assertThat(rect).isEqualTo(rect2)
    }

    @Test
    fun set2() {
        val rect = KtRect(0, 0, 10, 10)
        val rect2 = KtRect(20, 20, 50, 50)
        val expected = KtRect(20, 20, 50, 50)
        rect.set(rect2)
        assertThat(rect).isEqualTo(expected)
    }

    @Test
    fun inset() {
        val rect = KtRect(0, 0, 10, 10)
        val expected = KtRect(2, 2, 8, 8)
        rect.inset(2, 2)
        assertThat(rect).isEqualTo(expected)
    }

//    @Test
//    fun inset00() {
//        val rect = KtRect(0, 0, 10, 10)
//        val expected = KtRect(0, 0, 10, 10)
//        rect.inset(0, 0)
//        assertThat(rect).isEqualTo(expected)
//    }

    @Test
    fun inset00() {
        val rect = KtRect(0, 0, 10, 10)
        val expected = KtRect(0, 0, 10, 10)
        rect.inset(0, 0)
        assertThat(rect).isEqualTo(expected)
        rect.inset(0, 10)
        assertThat(rect).isEqualTo(KtRect(left = 0, top = 10, right = 10, bottom = 0))
        rect.inset(10, 0)
        assertThat(rect).isEqualTo(KtRect(left = 10, top = 10, right = 0, bottom = 0))
    }

    @Test
    fun inset2() {
        val rect = KtRect(0, 0, 10, 10)
        val expected = KtRect(2, 2, 8, 8)
        rect.inset(2, 2, 2, 2)
        assertThat(rect).isEqualTo(expected)
    }

    @Test
    fun sort() {
        val rect = KtRect(0, 0, 10, 10)
        val expected = KtRect(0, 0, 10, 10)
        rect.sort()
        assertThat(rect).isEqualTo(expected)

        val rect2 = KtRect(10, 0, 0, 10)
        val expected2 = KtRect(0, 0, 10, 10)
        rect2.sort()
        assertThat(rect2).isEqualTo(expected2)
    }

    @Test
    fun toIntArrayWithPositiveIntegers() {
        // Check if the function correctly converts a KtRect with all positive integers to an IntArray.
        val rect = KtRect(10, 20, 30, 40)
        val expectedArray = intArrayOf(10, 20, 30, 40)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithNegativeIntegers() {
        // Check if the function correctly converts a KtRect with all negative integers to an IntArray.
        val rect = KtRect(-10, -20, -30, -40)
        val expectedArray = intArrayOf(-10, -20, -30, -40)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithMixedIntegers() {
        // Check if the function correctly converts a KtRect with a mix of positive and negative integers to an IntArray.
        val rect = KtRect(-10, 20, 30, -40)
        val expectedArray = intArrayOf(-10, 20, 30, -40)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithZeroValues() {
        // Check if the function correctly converts a KtRect with all zero values (KtRect.EMPTY) to an IntArray.
        val rect = KtRect(0, 0, 0, 0)
        val expectedArray = intArrayOf(0, 0, 0, 0)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithIntMaxValue() {
        // Check if the function correctly converts a KtRect with Int.MAX_VALUE for all properties to an IntArray.
        val rect = KtRect(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        val expectedArray = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun toIntArrayWithIntMinValue() {
        // Check if the function correctly converts a KtRect with Int.MIN_VALUE for all properties to an IntArray.
        val rect = KtRect(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        val expectedArray = intArrayOf(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        assertThat(rect.toIntArray()).isEqualTo(expectedArray)
    }

    @Test
    fun widthWithPositiveIntegers() {
        // Check if the width is calculated correctly for a standard case where right > left.
        val rect = KtRect(10, 20, 30, 40)
        assertThat(rect.width).isEqualTo(20)
    }

    @Test
    fun widthResultingInZero() {
        // Check if the width is calculated as 0 when right equals left.
        val rect = KtRect(10, 20, 10, 40)
        assertThat(rect.width).isEqualTo(0)
    }

    @Test
    fun widthWithNegativeCoordinates() {
        // Check if the width is calculated correctly when both right and left are negative.
        val rect = KtRect(-10, 20, -30, 40)
        assertThat(rect.width).isEqualTo(-20)
    }

    @Test
    fun widthWithInvertedCoordinates() {
        // Check if the width is calculated as a negative value when left > right.
        val rect = KtRect(30, 20, 10, 40)
        assertThat(rect.width).isEqualTo(-20)
    }

    @Test
    fun widthIntegerOverflowCheck() {
        // Test for integer overflow when calculating width, e.g., right = Int.MAX_VALUE and left = Int.MIN_VALUE. [16]
        val rect = KtRect(Int.MIN_VALUE, 20, Int.MAX_VALUE, 40)
        assertThat(rect.width).isEqualTo(-1)
    }

    @Test
    fun widthIntegerUnderflowCheck() {
        // Test for integer underflow when calculating width, e.g., right = Int.MIN_VALUE and left = Int.MAX_VALUE.
        val rect = KtRect(Int.MAX_VALUE, 20, Int.MIN_VALUE, 40)
        assertThat(rect.width).isEqualTo(1)
    }

    @Test
    fun heightWithPositiveIntegers() {
        // Check if the height is calculated correctly for a standard case where bottom > top.
        val rect = KtRect(10, 20, 30, 40)
        assertThat(rect.height).isEqualTo(20)
        assertThat(rect.height()).isEqualTo(20)
    }

    @Test
    fun heightResultingInZero() {
        // Check if the height is calculated as 0 when bottom equals top.
        val rect = KtRect(10, 20, 30, 20)
        assertThat(rect.height).isEqualTo(0)
        assertThat(rect.height()).isEqualTo(0)
    }

    @Test
    fun heightWithNegativeCoordinates() {
        // Check if the height is calculated correctly when both bottom and top are negative.
        val rect = KtRect(10, -20, 30, -40)
        assertThat(rect.height).isEqualTo(-20)
        assertThat(rect.height()).isEqualTo(-20)
    }

    @Test
    fun heightWithInvertedCoordinates() {
        // Check if the height is calculated as a negative value when top > bottom.
        val rect = KtRect(10, 40, 30, 20)
        assertThat(rect.height).isEqualTo(-20)
        assertThat(rect.height()).isEqualTo(-20)
    }

    @Test
    fun heightIntegerOverflowCheck() {
        // Test for integer overflow when calculating height, e.g., bottom = Int.MAX_VALUE and top = Int.MIN_VALUE. [16]
        val rect = KtRect(10, Int.MIN_VALUE, 30, Int.MAX_VALUE)
        assertThat(rect.height).isEqualTo(-1)
        assertThat(rect.height()).isEqualTo(-1)
    }

    @Test
    fun heightIntegerUnderflowCheck() {
        // Test for integer underflow when calculating height, e.g., bottom = Int.MIN_VALUE and top = Int.MAX_VALUE.
        val rect = KtRect(10, Int.MAX_VALUE, 30, Int.MIN_VALUE)
        assertThat(rect.height).isEqualTo(1)
        assertThat(rect.height()).isEqualTo(1)
    }

    @Test
    fun emptyObjectWidthCalculation() {
        // Verify that the width of the KtRect.EMPTY companion object is 0.
        val rect = KtImmutableRect.EMPTY.toMutable()
        assertThat(rect.width).isEqualTo(0)
    }

    @Test
    fun emptyObjectHeightCalculation() {
        // Verify that the height of the KtRect.EMPTY companion object is 0.
        val rect = KtImmutableRect.EMPTY.toMutable()
        assertThat(rect.height).isEqualTo(0)
        assertThat(rect.height()).isEqualTo(0)
    }

    @Test
    fun union() {
        val rect1 = KtRect(0, 0, 10, 10)
        val rect2 = KtRect(5, 5, 15, 15)
        val expected = KtRect(0, 0, 15, 15)
        rect1.union(rect2)
        assertThat(rect1).isEqualTo(expected)

        val emptyRect = KtImmutableRect.EMPTY.toMutable()
        rect1.union(emptyRect)
        assertThat(rect1).isEqualTo(rect1)
        emptyRect.union(rect1)
        assertThat(emptyRect).isEqualTo(rect1)
    }

    @Test
    fun unionEmpty() {
        val rect1 = KtRect()
        val rect2 = KtRect(5, 5, 15, 15)
        rect1.union(rect2)
        assertThat(rect1).isEqualTo(rect2)

        val emptyRect = KtImmutableRect.EMPTY.toMutable()
        rect1.union(emptyRect)
        assertThat(rect1).isEqualTo(rect1)
        emptyRect.union(rect1)
        assertThat(emptyRect).isEqualTo(rect1)
    }

    @Test
    fun union2() {
        val rect1 = KtRect(0, 0, 10, 10)
        val expected = KtRect(0, 0, 15, 15)
        rect1.union(5, 5, 15, 15)
        assertThat(rect1).isEqualTo(expected)
    }

    @Test
    fun union2Empty() {
        val rect1 = KtRect()
        val other = KtRect(5, 5, 15, 15)
        rect1.union(5, 5, 15, 15)
        assertThat(rect1).isEqualTo(other)
    }

    @Test
    fun union3() {
        val rect1 = KtRect(0, 0, 10, 10)
        val expected = KtRect(0, 0, 15, 15)
        rect1.union(15, 15)
        assertThat(rect1).isEqualTo(expected)
    }

    @Test
    fun union3Empty() {
        val rect1 = KtRect()
        rect1.union(15, 15)
        assertThat(rect1).isEqualTo(rect1)
    }

    @Test
    fun intersect() {
        val rect1 = KtRect(0, 0, 10, 10)
        val rect2 = KtRect(5, 5, 15, 15)
        val expected = KtRect(5, 5, 10, 10)
        rect1.intersect(rect2)
        assertThat(rect1).isEqualTo(expected)

        val rect1Unprocessed = KtRect(0, 0, 10, 10)
        val rect3 = KtRect(0, 0, 10, 10)
        rect3.intersect(rect1Unprocessed)
        assertThat(rect3).isEqualTo(rect1Unprocessed)
        rect1Unprocessed.intersect(rect3)
        assertThat(rect1Unprocessed).isEqualTo(rect1Unprocessed)
    }

    @Test
    fun intersectAEmpty() {
        val rect1 = KtRect()
        val rect2 = KtRect(5, 5, 15, 15)
        val expected = KtRect()
        rect1.intersect(rect2)
        assertThat(rect1).isEqualTo(expected)
        rect2.intersect(rect1)
        assertThat(rect2).isEqualTo(KtRect(5, 5, 15, 15))
    }

    @Test
    fun intersect2() {
        val rect1 = KtRect(0, 0, 10, 10)
        val expected = KtRect(5, 5, 10, 10)
        rect1.intersect(5, 5, 15, 15)
        assertThat(rect1).isEqualTo(expected)

        val rect1Unprocessed = KtRect(0, 0, 10, 10)
        val rect3 = KtRect(0, 0, 10, 10)
        rect3.intersect(0, 0, 10, 10)
        assertThat(rect3).isEqualTo(rect1Unprocessed)
        rect1Unprocessed.intersect(0, 0, 10, 10)
        assertThat(rect1Unprocessed).isEqualTo(rect1Unprocessed)
    }

    @Test
    fun intersect2Empty() {
        val rect1 = KtRect()
        rect1.intersect(5, 5, 15, 15)
        assertThat(rect1).isEqualTo(KtRect())
    }

    @Test
    fun contains() {
        val rect = KtRect(0, 0, 10, 10)
        assertThat(rect.contains(5, 5)).isTrue()
        assertThat(rect.contains(0, 0)).isTrue() // Inclusive left/top
        assertThat(rect.contains(10, 10)).isFalse() // Exclusive right/bottom
        assertThat(rect.contains(11, 5)).isFalse()

        val insideRect = KtRect(2, 2, 8, 8)
        assertThat(rect.contains(insideRect)).isTrue()
        assertThat(rect.contains(2, 2, 8, 8)).isTrue()

        val overlappingRect = KtRect(5, 5, 15, 15)
        assertThat(rect.contains(overlappingRect)).isFalse()
        assertThat(rect.contains(5, 5, 15, 15)).isFalse()

        val insideRectF = KtRectF(2f, 2f, 8f, 8f)
        assertThat(rect.contains(insideRectF)).isTrue()

        val overlappingRectF = KtRectF(5f, 5f, 15f, 15f)
        assertThat(rect.contains(overlappingRectF)).isFalse()
    }

    @Test
    fun intersects() {
        val rect1 = KtRect(0, 0, 10, 10)
        val rect2 = KtRect(5, 5, 15, 15)
        assertThat(rect1.intersects(rect2)).isTrue()
        assertThat(rect1.intersects(5, 5, 15, 15)).isTrue()

        val rect3 = KtRect(20, 20, 30, 30)
        assertThat(rect1.intersects(rect3)).isFalse()
        assertThat(rect1.intersects(20, 20, 30, 30)).isFalse()

        // Touching edges usually considered intersecting in Android RectF?
        // RectF.intersects(a, b) -> left < right && top < bottom ...
        // If left == right, it returns false (empty).
        // If r1.right == r2.left, 10 < 10 is false -> no intersection.
        val rect4 = KtRect(10, 0, 20, 10)
        assertThat(rect1.intersects(rect4)).isTrue()
        assertThat(rect1.intersects(10, 0, 20, 10)).isTrue()
    }

    @Test
    fun width() {
        val rect1 = KtRect(0, 0, 10, 10)
        assertThat(rect1.width).isEqualTo(10)
        assertThat(rect1.width()).isEqualTo(10)

        val rect2 = KtRect(10, 0, 20, 10)
        assertThat(rect2.width).isEqualTo(10)
        assertThat(rect2.width()).isEqualTo(10)
    }

    @Test
    fun height() {
        val rect1 = KtRect(0, 0, 10, 10)
        assertThat(rect1.height).isEqualTo(10)

        val rect2 = KtRect(10, 0, 20, 20)
        assertThat(rect2.height).isEqualTo(20)
    }

    @Test
    fun center() {
        val rect1 = KtRect(0, 0, 10, 10)
        assertThat(rect1.centerX).isEqualTo(5)
        assertThat(rect1.centerX()).isEqualTo(5)
        assertThat(rect1.centerY).isEqualTo(5)
        assertThat(rect1.centerY()).isEqualTo(5)

        val rect2 = KtRect(10, 0, 20, 20)
        assertThat(rect2.centerX).isEqualTo(15)
        assertThat(rect2.centerX()).isEqualTo(15)
        assertThat(rect2.centerY).isEqualTo(10)
        assertThat(rect2.centerY()).isEqualTo(10)
    }

    @Test
    fun isEmpty() {
        val rect1 = KtRect(0, 0, 10, 10)
        assertThat(rect1.isEmpty).isFalse()

        val rect2 = KtImmutableRect.EMPTY.toMutable()
        assertThat(rect2.isEmpty).isTrue()
    }

    @Test
    fun offset() {
        val rect = KtRect(0, 0, 10, 10)
        val expected = KtRect(5, 5, 15, 15)
        rect.offset(5, 5)
        assertThat(rect).isEqualTo(expected)
    }

    @Test
    fun offsetTo() {
        val rect = KtRect(0, 0, 10, 10)
        val expected = KtRect(5, 5, 15, 15)
        rect.offsetTo(5, 5)
        assertThat(rect).isEqualTo(expected)
    }

    @Test
    fun setEmpty() {
        val rect = KtRect(0, 0, 10, 10)
        rect.setEmpty()
        assertThat(rect).isEqualTo(KtRect().apply { set(0, 0, 0, 0) })
    }

    @Test
    fun toImmutable() {
        val rect = KtRect(1, 2, 3, 4)
        val mutable = rect.toImmutable()
        assertThat(mutable).isInstanceOf(KtImmutableRect::class)
        assertThat(mutable.left).isEqualTo(1)
        assertThat(mutable.top).isEqualTo(2)
        assertThat(mutable.right).isEqualTo(3)
        assertThat(mutable.bottom).isEqualTo(4)
    }

    @Test
    fun toIntArray() {
        val rect = KtRect(1, 2, 3, 4)
        val ints = rect.toIntArray()
        assertThat(ints[0]).isEqualTo(1)
        assertThat(ints[1]).isEqualTo(2)
        assertThat(ints[2]).isEqualTo(3)
        assertThat(ints[3]).isEqualTo(4)
    }
}
