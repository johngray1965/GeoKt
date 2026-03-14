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

@file:Suppress("unused", "TooManyFunctions")

package io.legere.geokt

const val THREE_BY_THREE = 9
const val SCALE_X = 0
const val SKEW_X = 1
const val TRANS_X = 2
const val SKEW_Y = 3
const val SCALE_Y = 4
const val TRANS_Y = 5
const val PERSP_0 = 6
const val PERSP_1 = 7
const val PERSP_2 = 8



interface MatrixValues {
    val values: DoubleArray

    fun isIdentity(): Boolean = values.isIdentity()

    fun isAffine(): Boolean = values.isAffine()

    // --- Mapping functions ---

    fun mapRect(rect: KtRectF) = values.mapRect(rect)

    fun mapRect(
        dst: KtRectF,
        src: FloatRectValues,
    ) = values.mapRect(dst, src)

    fun mapRadius(radius: Float): Float = values.mapRadius(radius)

    fun mapRadius(radius: Double): Double = values.mapRadius(radius)

    fun mapPoints(pts: FloatArray) = values.mapPoints(pts)

    fun mapPoints(pts: DoubleArray) = values.mapPoints(pts)

    fun mapPoints(
        dst: FloatArray,
        src: FloatArray,
    ) = values.mapPoints(dst, src)

    fun mapPoints(
        dst: DoubleArray,
        src: DoubleArray,
    ) = values.mapPoints(dst, src)

    fun mapPoints(
        dst: FloatArray,
        dstIndex: Int,
        src: FloatArray,
        srcIndex: Int,
        pointCount: Int,
    ) = values.mapPoints(dst, dstIndex, src, srcIndex, pointCount)

    fun mapPoints(
        dst: DoubleArray,
        dstIndex: Int,
        src: DoubleArray,
        srcIndex: Int,
        pointCount: Int,
    ) = values.mapPoints(dst, dstIndex, src, srcIndex, pointCount)

    fun mapVectors(vecs: FloatArray) = values.mapVectors(vecs)

    fun mapVectors(vecs: DoubleArray) = values.mapVectors(vecs)

    fun mapVectors(
        dest: FloatArray,
        src: FloatArray,
    ) = values.mapVectors(dest, src)

    fun mapVectors(
        dest: DoubleArray,
        src: DoubleArray,
    ) = values.mapVectors(dest, src)

    fun mapVectors(
        dst: FloatArray,
        dstIndex: Int,
        src: FloatArray,
        srcIndex: Int,
        vectorCount: Int,
    ) = values.mapVectors(dst, dstIndex, src, srcIndex, vectorCount)

    fun mapVectors(
        dst: DoubleArray,
        dstIndex: Int,
        src: DoubleArray,
        srcIndex: Int,
        vectorCount: Int,
    ) = values.mapVectors(dst, dstIndex, src, srcIndex, vectorCount)
}

data class KtMatrix(
    override val values: DoubleArray =
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
) : MatrixValues {
    constructor(other: MatrixValues) : this(other.values.copyOf())

    fun reset(): KtMatrix {
        values.reset()
        return this
    }

    fun set(src: MatrixValues): KtMatrix {
        src.values.copyInto(values, endIndex = THREE_BY_THREE)
        return this
    }

    fun set(src: DoubleArray): KtMatrix {
        src.copyInto(values, endIndex = THREE_BY_THREE)
        return this
    }

    fun setTranslate(
        dx: Float,
        dy: Float,
    ): KtMatrix {
        values.setTranslate(dx, dy)
        return this
    }

    fun setTranslate(
        dx: Double,
        dy: Double,
    ): KtMatrix {
        values.setTranslate(dx, dy)
        return this
    }

    fun setScale(
        sx: Float,
        sy: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.setScale(sx, sy, px, py)
        return this
    }

    fun setScale(
        sx: Double,
        sy: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.setScale(sx, sy, px, py)
        return this
    }

    fun setScale(
        sx: Float,
        sy: Float,
    ): KtMatrix {
        values.setScale(sx, sy)
        return this
    }

    fun setScale(
        sx: Double,
        sy: Double,
    ): KtMatrix {
        values.setScale(sx, sy)
        return this
    }

    fun setRotate(
        degrees: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.setRotate(degrees, px, py)
        return this
    }

    fun setRotate(
        degrees: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.setRotate(degrees, px, py)
        return this
    }

    fun setRotate(degrees: Float): KtMatrix {
        values.setRotate(degrees)
        return this
    }

    fun setRotate(degrees: Double): KtMatrix {
        values.setRotate(degrees)
        return this
    }

    fun setSkew(
        kx: Float,
        ky: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.setSkew(kx, ky, px, py)
        return this
    }

    fun setSkew(
        kx: Double,
        ky: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.setSkew(kx, ky, px, py)
        return this
    }

    fun setSkew(
        kx: Float,
        ky: Float,
    ): KtMatrix {
        values.setSkew(kx, ky)
        return this
    }

    fun setSkew(
        kx: Double,
        ky: Double,
    ): KtMatrix {
        values.setSkew(kx, ky)
        return this
    }

    fun setPolyToPoly(
        src: FloatArray,
        srcIndex: Int,
        dst: FloatArray,
        dstIndex: Int,
        pointCount: Int,
    ): Boolean = values.setPolyToPoly(src, srcIndex, dst, dstIndex, pointCount)

    fun preTranslate(
        dx: Float,
        dy: Float,
    ): KtMatrix {
        values.preTranslate(dx, dy)
        return this
    }

    fun preTranslate(
        dx: Double,
        dy: Double,
    ): KtMatrix {
        values.preTranslate(dx, dy)
        return this
    }

    fun preScale(
        sx: Float,
        sy: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.preScale(sx, sy, px, py)
        return this
    }

    fun preScale(
        sx: Double,
        sy: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.preScale(sx, sy, px, py)
        return this
    }

    fun preScale(
        sx: Float,
        sy: Float,
    ): KtMatrix {
        values.preScale(sx, sy)
        return this
    }

    fun preScale(
        sx: Double,
        sy: Double,
    ): KtMatrix {
        values.preScale(sx, sy)
        return this
    }

    fun preRotate(
        degrees: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.preRotate(degrees, px, py)
        return this
    }

    fun preRotate(
        degrees: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.preRotate(degrees, px, py)
        return this
    }

    fun preRotate(degrees: Float): KtMatrix {
        values.preRotate(degrees)
        return this
    }

    fun preRotate(degrees: Double): KtMatrix {
        values.preRotate(degrees)
        return this
    }

    fun preSkew(
        kx: Float,
        ky: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.preSkew(kx, ky, px, py)
        return this
    }

    fun preSkew(
        kx: Double,
        ky: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.preSkew(kx, ky, px, py)
        return this
    }

    fun preSkew(
        kx: Float,
        ky: Float,
    ): KtMatrix {
        values.preSkew(kx, ky)
        return this
    }

    fun preSkew(
        kx: Double,
        ky: Double,
    ): KtMatrix {
        values.preSkew(kx, ky)
        return this
    }

    fun postTranslate(
        dx: Float,
        dy: Float,
    ): KtMatrix {
        values.postTranslate(dx, dy)
        return this
    }

    fun postTranslate(
        dx: Double,
        dy: Double,
    ): KtMatrix {
        values.postTranslate(dx, dy)
        return this
    }

    fun postScale(
        sx: Float,
        sy: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.postScale(sx, sy, px, py)
        return this
    }

    fun postScale(
        sx: Double,
        sy: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.postScale(sx, sy, px, py)
        return this
    }

    fun postScale(
        sx: Float,
        sy: Float,
    ): KtMatrix {
        values.postScale(sx, sy)
        return this
    }

    fun postScale(
        sx: Double,
        sy: Double,
    ): KtMatrix {
        values.postScale(sx, sy)
        return this
    }

    fun postRotate(
        degrees: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.postRotate(degrees, px, py)
        return this
    }

    fun postRotate(
        degrees: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.postRotate(degrees, px, py)
        return this
    }

    fun postRotate(degrees: Float): KtMatrix {
        values.postRotate(degrees)
        return this
    }

    fun postRotate(degrees: Double): KtMatrix {
        values.postRotate(degrees)
        return this
    }

    fun postSkew(
        kx: Float,
        ky: Float,
        px: Float,
        py: Float,
    ): KtMatrix {
        values.postSkew(kx, ky, px, py)
        return this
    }

    fun postSkew(
        kx: Double,
        ky: Double,
        px: Double,
        py: Double,
    ): KtMatrix {
        values.postSkew(kx, ky, px, py)
        return this
    }

    fun postSkew(
        kx: Float,
        ky: Float,
    ): KtMatrix {
        values.postSkew(kx, ky)
        return this
    }

    fun postSkew(
        kx: Double,
        ky: Double,
    ): KtMatrix {
        values.postSkew(kx, ky)
        return this
    }

    fun postConcat(other: MatrixValues): KtMatrix {
        values.postConcat(other.values)
        return this
    }

    fun preConcat(other: MatrixValues): KtMatrix {
        values.preConcat(other.values)
        return this
    }

    fun invert(target: MatrixValues): Boolean {
        val inv = values.invert()
        return if (inv != null) {
            inv.copyInto(target.values, endIndex = THREE_BY_THREE)
            true
        } else {
            false
        }
    }
    // --- Mapping functions (Mutable matrix can still return new values) ---

    fun toImmutable() = KtImmutableMatrix(values.copyOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as KtMatrix
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int = this::class.hashCode() * 31 + values.contentHashCode()
}

data class KtImmutableMatrix(
    override val values: DoubleArray =
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
) : MatrixValues {
    constructor(other: MatrixValues) : this(other.values.copyOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as KtImmutableMatrix
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int = this::class.hashCode() * 31 + values.contentHashCode()

    fun reset(): KtImmutableMatrix = KtImmutableMatrix()

    /**
     * Returns the inverse of this matrix, or null if it's not invertible.
     */
    fun invert(): KtImmutableMatrix? = values.invert()?.let { KtImmutableMatrix(it) }

    // --- Immutable modifiers (return new instance) ---

    fun setPolyToPoly(
        src: FloatArray,
        srcIndex: Int,
        dst: FloatArray,
        dstIndex: Int,
        pointCount: Int,
    ): KtImmutableMatrix? {
        val result = DoubleArray(THREE_BY_THREE)
        return if (result.setPolyToPoly(src, srcIndex, dst, dstIndex, pointCount)) {
            KtImmutableMatrix(result)
        } else {
            null
        }
    }

    fun setTranslate(
        dx: Float,
        dy: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setTranslate(dx, dy) })

    fun setTranslate(
        dx: Double,
        dy: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setTranslate(dx, dy) })

    fun setScale(
        sx: Float,
        sy: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setScale(sx, sy, px, py) })

    fun setScale(
        sx: Double,
        sy: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setScale(sx, sy, px, py) })

    fun setScale(
        sx: Float,
        sy: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setScale(sx, sy) })

    fun setScale(
        sx: Double,
        sy: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setScale(sx, sy) })

    fun setRotate(
        degrees: Float,
        px: Float,
        py: Float,
    ) = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setRotate(degrees, px, py) })

    fun setRotate(
        degrees: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setRotate(degrees, px, py) })

    fun setRotate(degrees: Float): KtImmutableMatrix =
        KtImmutableMatrix(
            DoubleArray(
                THREE_BY_THREE,
            ).apply { setRotate(degrees) },
        )

    fun setRotate(degrees: Double): KtImmutableMatrix =
        KtImmutableMatrix(
            DoubleArray(
                THREE_BY_THREE,
            ).apply { setRotate(degrees) },
        )

    fun setSkew(
        kx: Float,
        ky: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setSkew(kx, ky, px, py) })

    fun setSkew(
        kx: Double,
        ky: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setSkew(kx, ky, px, py) })

    fun setSkew(
        kx: Float,
        ky: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setSkew(kx, ky) })

    fun setSkew(
        kx: Double,
        ky: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(DoubleArray(THREE_BY_THREE).apply { setSkew(kx, ky) })

    fun preConcat(other: MatrixValues): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preConcat(other.values) })

    fun preTranslate(
        dx: Float,
        dy: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preTranslate(dx, dy) })

    fun preTranslate(
        dx: Double,
        dy: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preTranslate(dx, dy) })

    fun preScale(
        sx: Float,
        sy: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preScale(sx, sy, px, py) })

    fun preScale(
        sx: Double,
        sy: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preScale(sx, sy, px, py) })

    fun preScale(
        sx: Float,
        sy: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preScale(sx, sy) })

    fun preScale(
        sx: Double,
        sy: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preScale(sx, sy) })

    fun preRotate(
        degrees: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preRotate(degrees, px, py) })

    fun preRotate(
        degrees: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preRotate(degrees, px, py) })

    fun preRotate(degrees: Float): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preRotate(degrees) })

    fun preRotate(degrees: Double): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preRotate(degrees) })

    fun preSkew(
        kx: Float,
        ky: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preSkew(kx, ky, px, py) })

    fun preSkew(
        kx: Double,
        ky: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preSkew(kx, ky, px, py) })

    fun preSkew(
        kx: Float,
        ky: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preSkew(kx, ky) })

    fun preSkew(
        kx: Double,
        ky: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { preSkew(kx, ky) })

    fun postTranslate(
        dx: Float,
        dy: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postTranslate(dx, dy) })

    fun postTranslate(
        dx: Double,
        dy: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postTranslate(dx, dy) })

    fun postScale(
        sx: Float,
        sy: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postScale(sx, sy, px, py) })

    fun postScale(
        sx: Double,
        sy: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postScale(sx, sy, px, py) })

    fun postScale(
        sx: Float,
        sy: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postScale(sx, sy) })

    fun postScale(
        sx: Double,
        sy: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postScale(sx, sy) })

    fun postRotate(
        degrees: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postRotate(degrees, px, py) })

    fun postRotate(
        degrees: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postRotate(degrees, px, py) })

    fun postRotate(degrees: Float): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postRotate(degrees) })

    fun postRotate(degrees: Double): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postRotate(degrees) })

    fun postSkew(
        kx: Float,
        ky: Float,
        px: Float,
        py: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postSkew(kx, ky, px, py) })

    fun postSkew(
        kx: Double,
        ky: Double,
        px: Double,
        py: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postSkew(kx, ky, px, py) })

    fun postSkew(
        kx: Float,
        ky: Float,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postSkew(kx, ky) })

    fun postSkew(
        kx: Double,
        ky: Double,
    ): KtImmutableMatrix = KtImmutableMatrix(values.copyOf().apply { postSkew(kx, ky) })

    fun postConcat(other: MatrixValues) = KtImmutableMatrix(values.copyOf().apply { postConcat(other.values) })

    fun toMutable() = KtMatrix(values.copyOf())

    companion object {
        val IDENTITY = KtImmutableMatrix()
    }
}
