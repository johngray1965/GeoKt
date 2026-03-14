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
import assertk.assertions.hasSize
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * This is a watered down version of android specific version
 * That verified the results with the Android Matrix.
 * That test runs as an Android test
 */
class MatrixComparisonTest {
    private lateinit var mutableMatrix: KtMatrix
    private lateinit var pdfMatrix: KtImmutableMatrix

    @BeforeTest
    fun setup() {
        pdfMatrix = KtImmutableMatrix()
        mutableMatrix = KtMatrix()
    }

    @Test
    fun resetBehavesLikePlatformMatrix() {
        // Modify both matrices first to ensure reset has an effect
        mutableMatrix.postTranslate(10f, 20f)

        mutableMatrix.reset()

        val result = pdfMatrix.postTranslate(10f, 20f).reset()

        assertThat(mutableMatrix.isIdentity()).isTrue()
        assertThat(result.isIdentity()).isTrue()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setTranslateBehavesLikePlatformMatrix() {
        val dx = 100f
        val dy = 200f

        mutableMatrix.setTranslate(dx, dy)
        val result = pdfMatrix.setTranslate(dx, dy)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setScaleBehavesLikePlatformMatrix() {
        val sx = 2f
        val sy = 0.5f

        mutableMatrix.setScale(sx, sy)
        val result = pdfMatrix.setScale(sx, sy)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setScaleWithOffsetBehavesLikePlatformMatrix() {
        val sx = 2f
        val sy = 0.5f

        mutableMatrix.setScale(sx, sy, 50f, 100f)
        val result = pdfMatrix.setScale(sx, sy, 50f, 100f)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setRotateBehavesLikePlatformMatrix() {
        val degrees = 45f

        mutableMatrix.setRotate(degrees)
        val result = pdfMatrix.setRotate(degrees)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setRotateWithOffsetBehavesLikePlatformMatrix() {
        val degrees = 45f

        mutableMatrix.setRotate(degrees, 50f, 100f)
        val result = pdfMatrix.setRotate(degrees, 50f, 100f)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setSkewBehavesLikePlatformMatrix() {
        val kx = 2f
        val ky = 3f

        mutableMatrix.setSkew(kx, ky)
        val result = pdfMatrix.setSkew(kx, ky)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun setSkewWithOffsetBehavesLikePlatformMatrix() {
        val kx = 2f
        val ky = 3f

        mutableMatrix.setSkew(kx, ky, 50f, 100f)
        val result = pdfMatrix.setSkew(kx, ky, 50f, 100f)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postTranslateBehavesLikePlatformMatrix() {
        val dx = 10f
        val dy = 20f

        mutableMatrix.postTranslate(dx, dy)
        var result = pdfMatrix.postTranslate(dx, dy)

        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postTranslate(5f, 5f)
        result = result.postTranslate(5f, 5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postScaleBehavesLikePlatformMatrix() {
        val sx = 2f
        val sy = 3f

        mutableMatrix.postScale(sx, sy)
        var result = pdfMatrix.postScale(sx, sy)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postScale(0.5f, 0.5f)
        result = result.postScale(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postScaleWithOffsetBehavesLikePlatformMatrix() {
        val sx = 2f
        val sy = 3f

        mutableMatrix.postScale(sx, sy, 50f, 100f)
        var result = pdfMatrix.postScale(sx, sy, 50f, 100f)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postScale(0.5f, 0.5f)
        result = result.postScale(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postRotateBehavesLikePlatformMatrix() {
        val degrees = 30f

        mutableMatrix.postRotate(degrees)
        var result = pdfMatrix.postRotate(degrees)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postRotate(60f)
        result = result.postRotate(60f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postRotateWithOffsetBehavesLikePlatformMatrix() {
        val degrees = 30f

        mutableMatrix.postRotate(degrees, 50f, 100f)
        var result = pdfMatrix.postRotate(degrees, 50f, 100f)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postRotate(60f)
        result = result.postRotate(60f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postSkewBehavesLikePlatformMatrix() {
        val kx = 2f
        val ky = 3f

        mutableMatrix.postSkew(kx, ky)
        var result = pdfMatrix.postSkew(kx, ky)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postSkew(0.5f, 0.5f)
        result = result.postSkew(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postSkewWithOffsetBehavesLikePlatformMatrix() {
        val kx = 2f
        val ky = 3f

        mutableMatrix.postSkew(kx, ky, 50f, 100f)
        var result = pdfMatrix.postSkew(kx, ky, 50f, 100f)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.postSkew(0.5f, 0.5f)
        result = result.postSkew(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preTranslateBehavesLikePlatformMatrix() {
        val dx = 10f
        val dy = 20f

        mutableMatrix.preTranslate(dx, dy)

        var result = pdfMatrix.preTranslate(dx, dy)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preTranslate(5f, 5f)
        result = result.preTranslate(5f, 5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preScaleBehavesLikePlatformMatrix() {
        val sx = 2f
        val sy = 3f

        mutableMatrix.preScale(sx, sy)
        var result = pdfMatrix.preScale(sx, sy)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preScale(0.5f, 0.5f)
        result = result.preScale(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preScaleWithOffsetBehavesLikePlatformMatrix() {
        val sx = 2f
        val sy = 3f

        mutableMatrix.preScale(sx, sy, 50f, 100f)
        var result = pdfMatrix.preScale(sx, sy, 50f, 100f)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preScale(0.5f, 0.5f)
        result = result.preScale(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preRotateBehavesLikePlatformMatrix() {
        val degrees = 30f

        mutableMatrix.preRotate(degrees)
        var result = pdfMatrix.preRotate(degrees)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preRotate(60f)
        result = result.preRotate(60f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preRotateWithOffsetBehavesLikePlatformMatrix() {
        val degrees = 30f

        mutableMatrix.preRotate(degrees, 50f, 100f)
        var result = pdfMatrix.preRotate(degrees, 50f, 100f)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preRotate(60f)
        result = result.preRotate(60f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preSkewBehavesLikePlatformMatrix() {
        val kx = 2f
        val ky = 3f

        mutableMatrix.preSkew(kx, ky)
        var result = pdfMatrix.preSkew(kx, ky)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preSkew(0.5f, 0.5f)
        result = result.preSkew(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preSkewWithOffsetBehavesLikePlatformMatrix() {
        val kx = 2f
        val ky = 3f

        mutableMatrix.preSkew(kx, ky, 50f, 100f)
        var result = pdfMatrix.preSkew(kx, ky, 50f, 100f)
        assertMatrixValuesEqual(mutableMatrix, result)

        // Chained operations
        mutableMatrix.preSkew(0.5f, 0.5f)
        result = result.preSkew(0.5f, 0.5f)
        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun postConcatBehavesLikePlatformMatrix() {
        val otherCustom = KtMatrix()

        otherCustom.setTranslate(10f, 20f)

        mutableMatrix.setScale(2f, 2f)
        var result = pdfMatrix.setScale(2f, 2f)

        mutableMatrix.postConcat(otherCustom)
        result = result.postConcat(otherCustom)

        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun preConcatBehavesLikePlatformMatrix() {
        val otherCustom = KtMatrix()

        otherCustom.setTranslate(10f, 20f)

        mutableMatrix.setScale(2f, 2f)
        var result = pdfMatrix.setScale(2f, 2f)

        mutableMatrix.preConcat(otherCustom)
        result = result.preConcat(otherCustom)

        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun invertBehavesLikePlatformMatrix() {
        mutableMatrix.setScale(2f, 2f)
        mutableMatrix.postTranslate(10f, 20f)

        val result = pdfMatrix.setScale(2f, 2f).postTranslate(10f, 20f)

        val customInverse = KtMatrix()

        mutableMatrix.invert(customInverse)
        val result2 = result.invert()

        println("customInverse: ${customInverse.values.contentToString()}")
        println("result2: ${result2?.values.contentToString()}")

        assertMatrixValuesEqual(mutableMatrix, result)
        assertMatrixValuesEqual(customInverse, result2!!)
    }

    @Test
    fun invertBehavesLikePlatformMatrix2() {
        val expected =
            doubleArrayOf(
                1.0194546,
                0.0,
                -20.997844837965395,
                0.0,
                1.4150456,
                -4.2376478374130215,
                0.0,
                0.0,
                1.0
            )
        mutableMatrix.set(
            doubleArrayOf(
                1.0194546,
                0.0,
                10.449388,
                0.0,
                1.4150456,
                -23.396986,
                0.0,
                0.0,
                1.0,
            ),
        )
        mutableMatrix.preTranslate(-30.847115f, 13.539732f)
        assertThat(mutableMatrix.values).isEqualTo(expected)
    }

    @Test
    fun isIdentityBehavesLikePlatformMatrix() {
        assertThat(mutableMatrix.isIdentity()).isTrue()
        assertThat(mutableMatrix.isIdentity()).isTrue()

        mutableMatrix.postTranslate(1f, 0f)
        val result = pdfMatrix.postTranslate(1f, 0f)

        assertThat(mutableMatrix.isIdentity()).isFalse()
        assertThat(result.isIdentity()).isFalse()
    }

    @Test
    fun isAffineBehavesLikePlatformMatrix() {
        assertThat(mutableMatrix.isAffine()).isTrue() // Identity is affine

        mutableMatrix.postScale(2f, 2f)
        val result = pdfMatrix.postScale(2f, 2f)
        assertThat(mutableMatrix.isAffine()).isTrue()
        assertThat(result.isAffine()).isTrue()

        mutableMatrix.set(
            KtImmutableMatrix(
                doubleArrayOf(
                    1.0,
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    0.0,
                    0.1,
                    0.2,
                    1.0,
                ),
            ),
        )
        assertThat(mutableMatrix.isAffine()).isFalse()
    }

    @Test
    fun mapPointsSingleArgBehavesLikePlatformMatrix() {
        val points = floatArrayOf(0f, 0f, 10f, 10f, 5f, 5f)
        val customPoints = points.copyOf()
        val customPoints2 = points.copyOf()

        mutableMatrix.postTranslate(10f, 20f)
        val result = pdfMatrix.postTranslate(10f, 20f)

        mutableMatrix.mapPoints(customPoints)
        result.mapPoints(customPoints2)

        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun mapPointsDoubleSingleArgBehavesLikePlatformMatrix() {
        val points = doubleArrayOf(0.0, 0.0, 10.0, 10.0, 5.0, 5.0)
        val customPoints = points.copyOf()
        val customPoints2 = points.copyOf()

        mutableMatrix.postTranslate(10f, 20f)
        val result = pdfMatrix.postTranslate(10f, 20f)

        mutableMatrix.mapPoints(customPoints)
        result.mapPoints(customPoints2)

        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun mapPointsTwoArgsBehavesLikePlatformMatrix() {
        val srcPoints = floatArrayOf(0f, 0f, 10f, 10f, 5f, 5f)
        val customDstPoints = FloatArray(srcPoints.size)
        val customDstPoints2 = FloatArray(srcPoints.size)

        mutableMatrix.postScale(2f, 3f)
        val result = pdfMatrix.postScale(2f, 3f)

        mutableMatrix.mapPoints(customDstPoints, srcPoints)
        result.mapPoints(customDstPoints2, srcPoints)

        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun mapPointsDoubleTwoArgsBehavesLikePlatformMatrix() {
        val srcPoints = doubleArrayOf(0.0, 0.0, 10.0, 10.0, 5.0, 5.0)
        val customDstPoints = DoubleArray(srcPoints.size)
        val customDstPoints2 = DoubleArray(srcPoints.size)

        mutableMatrix.postScale(2f, 3f)
        val result = pdfMatrix.postScale(2f, 3f)

        mutableMatrix.mapPoints(customDstPoints, srcPoints)
        result.mapPoints(customDstPoints2, srcPoints)

        assertMatrixValuesEqual(mutableMatrix, result)
    }

    @Test
    fun mapPointsMultiArgBehavesLikePlatformMatrix() {
        val srcPoints = floatArrayOf(0f, 0f, 1f, 1f, 2f, 2f, 3f, 3f)
        val customDstPoints = FloatArray(srcPoints.size)
        val customDstPoints2 = FloatArray(srcPoints.size)

        mutableMatrix.postTranslate(10f, 10f)
        val result = pdfMatrix.postTranslate(10f, 10f)

        val srcIndex = 2
        val dstIndex = 4
        val pointCount = 2

        mutableMatrix.mapPoints(customDstPoints, dstIndex, srcPoints, srcIndex, pointCount)
        result.mapPoints(customDstPoints2, dstIndex, srcPoints, srcIndex, pointCount)

        assertFloatArraysEqual(customDstPoints, customDstPoints2, 0.001f)
    }

    @Test
    fun mapPointsFloatMultiArgBehavesLikePlatformMatrix() {
        val srcPoints = doubleArrayOf(0.0, 0.0, 10.0, 10.0, 5.0, 5.0)
        val customDstPoints = DoubleArray(srcPoints.size)
        val customDstPoints2 = DoubleArray(srcPoints.size)

        mutableMatrix.postTranslate(10f, 10f)
        val result = pdfMatrix.postTranslate(10f, 10f)

        val srcIndex = 2
        val dstIndex = 2
        val pointCount = 2

        mutableMatrix.mapPoints(customDstPoints, dstIndex, srcPoints, srcIndex, pointCount)
        result.mapPoints(customDstPoints2, dstIndex, srcPoints, srcIndex, pointCount)

        assertDoubleArraysEqual(customDstPoints, customDstPoints2, 0.001)
    }

    @Test
    fun mapRadiusBehavesLikePlatformMatrix() {
        val radius = 10f

        mutableMatrix.postScale(2f, 3f)
        var result = pdfMatrix.postScale(2f, 3f)

        val customMappedRadius = mutableMatrix.mapRadius(radius)
        val customMappedRadius2 = result.mapRadius(radius)
        assertThat(customMappedRadius).isCloseTo(customMappedRadius2, 0.001f)

        // With rotation
        mutableMatrix.reset()
        result = result.reset()
        mutableMatrix.postRotate(90f)
        result = result.postRotate(90f)
        mutableMatrix.postScale(2f, 3f)
        result = result.postScale(2f, 3f)

        val customMappedRadiusRotated = mutableMatrix.mapRadius(radius)
        val customMappedRadiusRotated2 = result.mapRadius(radius)

        assertThat(customMappedRadiusRotated).isCloseTo(customMappedRadiusRotated2, 0.001f)
    }

    @Test
    fun mapRadiusFloatBehavesLikePlatformMatrix() {
        val radius = 10.0

        mutableMatrix.postScale(2f, 3f)
        var result = pdfMatrix.postScale(2f, 3f)

        val customMappedRadius = mutableMatrix.mapRadius(radius)
        val customMappedRadius2 = result.mapRadius(radius)

        assertThat(customMappedRadius).isCloseTo(customMappedRadius2, 0.001)

        // With rotation
        mutableMatrix.reset()
        result = result.reset()
        mutableMatrix.postRotate(90f)
        result = result.postRotate(90f)
        mutableMatrix.postScale(2f, 3f)
        result = result.postScale(2f, 3f)

        val customMappedRadiusRotated = mutableMatrix.mapRadius(radius)
        val customMappedRadiusRotated2 = result.mapRadius(radius)
        assertThat(customMappedRadiusRotated).isCloseTo(customMappedRadiusRotated2, 0.001)

        assertThat(customMappedRadius).isCloseTo(customMappedRadius2, 0.001)
    }

    @Test
    fun mapRectSingleArgBehavesLikePlatformMatrix() {
        val customRect = KtImmutableRectF(0f, 0f, 100f, 200f)
        val customRect2 = KtImmutableRectF(0f, 0f, 100f, 200f)

        mutableMatrix.postTranslate(10f, 20f)
        mutableMatrix.postScale(0.5f, 2f)
        val result = pdfMatrix.postTranslate(10f, 20f).postScale(0.5f, 2f)

        val customResult = KtRectF(customRect)
        mutableMatrix.mapRect(customResult)
        val customResult2 = KtRectF(customRect2)
        result.mapRect(customResult2)

        assertThat(customResult.left).isCloseTo(customResult2.left, 0.001f)
        assertThat(customResult.top).isCloseTo(customResult2.top, 0.001f)
        assertThat(customResult.right).isCloseTo(customResult2.right, 0.001f)
        assertThat(customResult.bottom).isCloseTo(customResult2.bottom, 0.001f)
    }

    @Test
    fun mapRectTwoArgsBehavesLikePlatformMatrix() {
        val srcRect = KtRectF(0f, 0f, 100f, 200f)
        val customDstRect = KtRectF()
        val customDstRect2 = KtRectF()

        mutableMatrix.postTranslate(10f, 20f)
        mutableMatrix.postRotate(90f)
        val result = pdfMatrix.postTranslate(10f, 20f).postRotate(90f)

        mutableMatrix.mapRect(customDstRect, srcRect)
        result.mapRect(customDstRect2, srcRect)

//        assertThat(customResult).isEqualTo(platformResult)
        assertThat(customDstRect.left).isCloseTo(customDstRect2.left, 0.001f)
        assertThat(customDstRect.top).isCloseTo(customDstRect2.top, 0.001f)
        assertThat(customDstRect.right).isCloseTo(customDstRect2.right, 0.001f)
        assertThat(customDstRect.bottom).isCloseTo(customDstRect2.bottom, 0.001f)
    }

    @Test
    fun mapVectorsSingleArgBehavesLikePlatformMatrix() {
        val vectors = floatArrayOf(1f, 1f, 0f, 10f, -5f, 0f)
        val customVectors = vectors.copyOf()
        val customVectors2 = vectors.copyOf()

        mutableMatrix.postTranslate(100f, 200f) // Translation should be ignored by mapVectors
        mutableMatrix.postScale(2f, 3f)
        val result = pdfMatrix.postTranslate(100f, 200f).postScale(2f, 3f)

        mutableMatrix.mapVectors(customVectors)
        result.mapVectors(customVectors2)

        assertFloatArraysEqual(customVectors, customVectors2, 0.001f)
    }

    @Test
    fun mapVectorsDoubleSingleArgBehavesLikePlatformMatrix() {
        val vectors = doubleArrayOf(1.0, 1.0, 0.0, 10.0, -5.0, 0.0)
        val customVectors = vectors.copyOf()
        val customVectors2 = vectors.copyOf()

        mutableMatrix.postTranslate(100f, 200f) // Translation should be ignored by mapVectors
        mutableMatrix.postScale(2f, 3f)
        val result = pdfMatrix.postTranslate(100f, 200f).postScale(2f, 3f)

        mutableMatrix.mapVectors(customVectors)
        result.mapVectors(customVectors2)

        assertDoubleArraysEqual(customVectors, customVectors2, 0.001)
    }

    @Test
    fun mapVectorsTwoArgsBehavesLikePlatformMatrix() {
        val srcVectors = floatArrayOf(1f, 1f, 0f, 10f, -5f, 0f)
        val customDstVectors = FloatArray(srcVectors.size)
        val customDstVectors2 = FloatArray(srcVectors.size)

        mutableMatrix.postTranslate(100f, 200f)
        mutableMatrix.postRotate(45f)
        val result = pdfMatrix.postTranslate(100f, 200f).postRotate(45f)

        mutableMatrix.mapVectors(customDstVectors, srcVectors)
        result.mapVectors(customDstVectors2, srcVectors)

        assertFloatArraysEqual(customDstVectors, customDstVectors2, 0.001f)
    }

    @Test
    fun mapVectorsDoubleTwoArgsBehavesLikePlatformMatrix() {
        val srcVectors = doubleArrayOf(1.0, 1.0, 0.0, 10.0, -5.0, 0.0)
        val customDstVectors = DoubleArray(srcVectors.size)
        val customDstVectors2 = DoubleArray(srcVectors.size)

        mutableMatrix.postTranslate(100f, 200f)
        mutableMatrix.postRotate(45f)
        val result = pdfMatrix.postTranslate(100f, 200f).postRotate(45f)

        mutableMatrix.mapVectors(customDstVectors, srcVectors)
        result.mapVectors(customDstVectors2, srcVectors)

        assertDoubleArraysEqual(customDstVectors, customDstVectors2, 0.001)
    }

    @Test
    fun mapVectorsMultiArgBehavesLikePlatformMatrix() {
        val srcVectors = floatArrayOf(1f, 1f, 2f, 2f, 3f, 3f, 4f, 4f)
        val customDstVectors = FloatArray(srcVectors.size)
        val customDstVectors2 = FloatArray(srcVectors.size)

        mutableMatrix.postTranslate(10f, 10f)
        mutableMatrix.postScale(2f, 2f)
        val result = pdfMatrix.postTranslate(10f, 10f).postScale(2f, 2f)

        val srcIndex = 2
        val dstIndex = 4
        val vectorCount = 2

        mutableMatrix.mapVectors(customDstVectors, dstIndex, srcVectors, srcIndex, vectorCount)
        result.mapVectors(customDstVectors2, dstIndex, srcVectors, srcIndex, vectorCount)

        assertFloatArraysEqual(customDstVectors, customDstVectors2, 0.001f)
    }

    @Test
    fun mapVectorsFloatMultiArgBehavesLikePlatformMatrix() {
        val srcVectors = doubleArrayOf(1.0, 1.0, 2.0, 2.0, 3.0, 3.0, 4.0, 4.0)
        val customDstVectors = DoubleArray(srcVectors.size)
        val customDstVectors2 = DoubleArray(srcVectors.size)

        mutableMatrix.postTranslate(10f, 10f)
        mutableMatrix.postScale(2f, 2f)
        val result = pdfMatrix.postTranslate(10f, 10f).postScale(2f, 2f)

        val srcIndex = 2
        val dstIndex = 4
        val vectorCount = 2

        mutableMatrix.mapVectors(customDstVectors, dstIndex, srcVectors, srcIndex, vectorCount)
        result.mapVectors(customDstVectors2, dstIndex, srcVectors, srcIndex, vectorCount)

        assertDoubleArraysEqual(customDstVectors, customDstVectors2, 0.001)
    }


    private fun assertMatrixValuesEqual(
        matrixA: MatrixValues,
        matrixB: MatrixValues,
        delta: Double = 0.001,
    ) {
        assertDoubleArraysEqual(matrixA.values, matrixB.values, delta)
    }

    @Suppress("SameParameterValue")
    private fun assertFloatArraysEqual(
        expected: FloatArray,
        actual: FloatArray,
        delta: Float,
    ) {
        assertThat(actual).hasSize(expected.size)
        for (i in expected.indices) {
            assertThat(actual[i]).isCloseTo(expected[i], delta)
        }
    }


    private fun assertDoubleArraysEqual(
        expected: DoubleArray,
        actual: DoubleArray,
        delta: Double,
    ) {
        assertThat(actual).hasSize(expected.size)
        for (i in expected.indices) {
            assertThat(actual[i]).isCloseTo(expected[i], delta)
        }
    }
}