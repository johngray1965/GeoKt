@file:Suppress("unused", "TooManyFunctions")
package io.legere.geokt

interface FloatPointValues {
    val x: Float
    val y: Float
}

data class KtImmutablePointF(
    override val x: Float,
    override val y: Float,
) : FloatPointValues,
    ImmutablePointInterface<Float, KtImmutablePointF> {
    fun toFloatArray(): FloatArray = floatArrayOf(x, y)

    fun toMutable() = KtPointF(x, y)

    override fun offset(
        dx: Float,
        dy: Float,
    ) = KtImmutablePointF(x + dx, y + dy)

    override fun negate() = KtImmutablePointF(-x, -y)

    override fun length() = kotlin.math.sqrt(x * x + y * y)

    companion object {
        val ZERO = KtImmutablePointF(0f, 0f)
    }
}

data class KtPointF(
    override var x: Float = 0f,
    override var y: Float = 0f,
) : FloatPointValues,
    MutablePointInterface<Float, KtImmutablePointF> {
    fun toFloatArray(): FloatArray = floatArrayOf(x, y)

    fun set(
        x: Float,
        y: Float,
    ) {
        this.x = x
        this.y = y
    }

    fun set(src: FloatPointValues) {
        this.x = src.x
        this.y = src.y
    }

    fun toImmutable() = KtImmutablePointF(x, y)

    override fun offset(
        dx: Float,
        dy: Float,
    ) {
        x += dx
        y += dy
    }

    override fun negate() {
        x = -x
        y = -y
    }

    override fun length() = kotlin.math.sqrt(x * x + y * y)
}
