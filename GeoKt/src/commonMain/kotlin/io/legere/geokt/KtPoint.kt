@file:Suppress("unused", "TooManyFunctions")

package io.legere.geokt

interface IntPointValues {
    val x: Int
    val y: Int
}

data class KtImmutablePoint(
    override val x: Int,
    override val y: Int,
) : IntPointValues,
    ImmutablePointInterface<Int, KtImmutablePoint> {
    fun toIntArray(): IntArray = intArrayOf(x, y)

    fun toMutable() = KtPoint(x, y)

    override fun offset(
        dx: Int,
        dy: Int,
    ) = KtImmutablePoint(x + dx, y + dy)

    override fun negate() = KtImmutablePoint(-x, -y)

    override fun length(): Int = kotlin.math.sqrt((x * x + y * y).toDouble()).toInt()

    companion object {
        val ZERO = KtImmutablePoint(0, 0)
    }
}

data class KtPoint(
    override var x: Int = 0,
    override var y: Int = 0,
) : IntPointValues,
    MutablePointInterface<Int, KtPoint> {
    fun toIntArray(): IntArray = intArrayOf(x, y)

    fun set(
        x: Int,
        y: Int,
    ) {
        this.x = x
        this.y = y
    }

    fun set(src: IntPointValues) {
        this.x = src.x
        this.y = src.y
    }

    fun toImmutable() = KtImmutablePoint(x, y)

    override fun offset(
        dx: Int,
        dy: Int,
    ) {
        x += dx
        y += dy
    }

    override fun negate() {
        x = -x
        y = -y
    }

    override fun length(): Int = kotlin.math.sqrt((x * x + y * y).toDouble()).toInt()
}
