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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random
import android.graphics.Matrix as AndroidMatrix

@RunWith(AndroidJUnit4::class)
class MatrixStressTest {
    // 0: translate
    // 1: scale
    // 2: rotate
    // 3: skew
    // 4: postTranslate
    // 5: postScale
    // 6: postRotate
    // 7: postSkew
    private val operationCount = 100_000
    private val randomSeed = 42L

    // Pre-generate operations and parameters to avoid benchmarking Random
    enum class OpType {
        PRE_TRANSLATE,
        PRE_SCALE,
        PRE_ROTATE,
        PRE_SKEW,
        POST_TRANSLATE,
        POST_SCALE,
        POST_ROTATE,
        POST_SKEW,
    }

    data class Op(
        val type: OpType,
        val p1: Float,
        val p2: Float,
    )

    data class DoubleOp(
        val type: OpType,
        val p1: Double,
        val p2: Double,
    )
    private val operations: List<Op> by lazy {
        val rand = Random(randomSeed)
        List(operationCount) {
            val type = OpType.entries[rand.nextInt(
                OpType.entries.size)]
            val p1: Float
            val p2: Float

            when (type) {
                OpType.PRE_TRANSLATE, OpType.POST_TRANSLATE -> { // Translate
                    p1 = (rand.nextFloat() - 0.5f) * 10f
                    p2 = (rand.nextFloat() - 0.5f) * 10f
                }

                OpType.PRE_SCALE, OpType.POST_SCALE -> { // Scale - use a range like 0.9 to 1.1
                    p1 = 1.0f + (rand.nextFloat() - 0.5f) * 0.2f
                    p2 = 1.0f + (rand.nextFloat() - 0.5f) * 0.2f
                }

                OpType.PRE_ROTATE, OpType.POST_ROTATE -> { // Rotate
                    if (rand.nextBoolean()) {
                        // 50% chance for cardinal angles
                        p1 = listOf(0f, 90f, 180f, 270f).random(rand)
                    } else {
                        // 50% chance for random angles
                        p1 = (rand.nextFloat() - 0.5f) * 90f
                    }
                    p2 = 0f // Degrees only
                }

                else -> { // Skew
                    p1 = (rand.nextFloat() - 0.5f) * 0.1f
                    p2 = (rand.nextFloat() - 0.5f) * 0.1f
                }
            }
            Op(type, p1, p2)
        }
    }
    private val doubleOperations: List<DoubleOp> by lazy {
        val rand = Random(randomSeed)
        List(operationCount) {
            val type = OpType.entries[rand.nextInt(
                OpType.entries.size)]
            val p1: Double
            val p2: Double

            when (type) {
                OpType.PRE_TRANSLATE, OpType.POST_TRANSLATE -> { // Translate
                    p1 = (rand.nextDouble() - 0.5) * 10
                    p2 = (rand.nextDouble() - 0.5) * 10
                }

                OpType.PRE_SCALE, OpType.POST_SCALE -> { // Scale - use a range like 0.9 to 1.1
                    p1 = 1.0f + (rand.nextDouble() - 0.5) * 0.2
                    p2 = 1.0f + (rand.nextDouble() - 0.5) * 0.2
                }

                OpType.PRE_ROTATE, OpType.POST_ROTATE -> { // Rotate
                    if (rand.nextBoolean()) {
                        // 50% chance for cardinal angles
                        p1 = listOf(0.0, 90.0, 180.0, 270.0).random(rand)
                    } else {
                        // 50% chance for random angles
                        p1 = (rand.nextDouble() - 0.5) * 90
                    }
                    p2 = 0.0 // Degrees only
                }

                else -> { // Skew
                    p1 = (rand.nextDouble() - 0.5) * 0.1
                    p2 = (rand.nextDouble() - 0.5) * 0.1
                }
            }
            DoubleOp(type, p1, p2)
        }
    }

    @Test
    fun verifyCorrectness() {
        val pdfMatrix = KtMatrix()
        val androidMatrix = AndroidMatrix()

        for ((_, op) in operations.withIndex()) {
//            println("op: $i $op")
            runWithMutablePdfMatrix(pdfMatrix, op)
            runWithAndroidMatrix(androidMatrix, op)

//            val pdfValues = pdfMatrix.values.toFloatArray()
//            val androidValues = FloatArray(9)
//            androidMatrix.getValues(androidValues)

//            println("pdfValues:     ${pdfValues.contentToString()}")
//            println("androidValues: ${androidValues.contentToString()}")
//            assertThat(pdfValues).usingTolerance(0.01).containsExactly(androidValues)
//            assertMatrixClose(androidValues, pdfValues)
// )
//            assertWithMessage("expected: ${pdfValues.contentToString()}, actual: ${androidValues.contentToString()}")
//                .that(pdfValues)
//                .usingTolerance(0.1)
//                .containsExactly(androidValues)
        }
//
        val pdfValues = pdfMatrix.values.map { it.toFloat() }.toFloatArray()
        val androidValues = FloatArray(9)
        androidMatrix.getValues(androidValues)

        assertWithMessage("expected: ${pdfValues.contentToString()}, actual: ${androidValues.contentToString()}")
            .that(pdfValues)
            .usingTolerance(0.1)
            .containsExactly(androidValues)

//
//        // Using a small delta for floating point differences
//        for (i in 0 until 9) {
//            assertEquals("Value at index $i mismatch", androidValues[i], pdfValues[i], 0.01f)
//        }
    }
    private fun runWithMutablePdfMatrix(
        matrix: KtMatrix,
        op: Op,
    ) {
        when (op.type) {
            OpType.PRE_TRANSLATE -> matrix.preTranslate(op.p1, op.p2)
            OpType.PRE_SCALE -> matrix.preScale(op.p1, op.p2)
            OpType.PRE_ROTATE -> matrix.preRotate(op.p1)
            OpType.PRE_SKEW -> matrix.preSkew(op.p1, op.p2)
            OpType.POST_TRANSLATE -> matrix.postTranslate(op.p1, op.p2)
            OpType.POST_SCALE -> matrix.postScale(op.p1, op.p2)
            OpType.POST_ROTATE -> matrix.postRotate(op.p1)
            OpType.POST_SKEW -> matrix.postSkew(op.p1, op.p2)
        }
    }

    private fun runWithMutablePdfMatrix(
        matrix: KtMatrix,
        op: DoubleOp,
    ) {
        when (op.type) {
            OpType.PRE_TRANSLATE -> matrix.preTranslate(op.p1, op.p2)
            OpType.PRE_SCALE -> matrix.preScale(op.p1, op.p2)
            OpType.PRE_ROTATE -> matrix.preRotate(op.p1)
            OpType.PRE_SKEW -> matrix.preSkew(op.p1, op.p2)
            OpType.POST_TRANSLATE -> matrix.postTranslate(op.p1, op.p2)
            OpType.POST_SCALE -> matrix.postScale(op.p1, op.p2)
            OpType.POST_ROTATE -> matrix.postRotate(op.p1)
            OpType.POST_SKEW -> matrix.postSkew(op.p1, op.p2)
        }
    }

    private fun runWithAndroidMatrix(
        matrix: AndroidMatrix,
        op: Op,
    ) {
        when (op.type) {
            OpType.PRE_TRANSLATE -> matrix.preTranslate(op.p1, op.p2)
            OpType.PRE_SCALE -> matrix.preScale(op.p1, op.p2)
            OpType.PRE_ROTATE -> matrix.preRotate(op.p1)
            OpType.PRE_SKEW -> matrix.preSkew(op.p1, op.p2)
            OpType.POST_TRANSLATE -> matrix.postTranslate(op.p1, op.p2)
            OpType.POST_SCALE -> matrix.postScale(op.p1, op.p2)
            OpType.POST_ROTATE -> matrix.postRotate(op.p1)
            OpType.POST_SKEW -> matrix.postSkew(op.p1, op.p2)
        }
    }

}