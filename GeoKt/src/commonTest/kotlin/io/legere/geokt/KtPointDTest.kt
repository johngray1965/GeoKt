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
 */

package io.legere.geokt

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class KtPointDTest {
    @Test
    fun constructorsAndWidening() {
        assertThat(KtImmutablePointD()).isEqualTo(KtImmutablePointD(0.0, 0.0))
        assertThat(KtImmutablePointD(KtImmutablePointF(1.5f, 2.5f))).isEqualTo(KtImmutablePointD(1.5, 2.5))
    }

    @Test
    fun offsetNegateLength() {
        assertThat(KtImmutablePointD(1.0, 2.0).offset(3.0, 4.0)).isEqualTo(KtImmutablePointD(4.0, 6.0))
        assertThat(KtImmutablePointD(1.0, 2.0).negate()).isEqualTo(KtImmutablePointD(-1.0, -2.0))
        assertThat(KtImmutablePointD(3.0, 4.0).length()).isEqualTo(5.0)
    }

    @Test
    fun mutableSetAndConversions() {
        val p = KtPointD()
        p.set(1.0, 2.0)
        assertThat(p.toImmutable()).isEqualTo(KtImmutablePointD(1.0, 2.0))
        p.offset(1.0, 1.0)
        assertThat(p).isEqualTo(KtPointD(2.0, 3.0))
        assertThat(p.toFloat()).isEqualTo(KtImmutablePointF(2.0f, 3.0f))
        assertThat(p.toDoubleArray()).isEqualTo(doubleArrayOf(2.0, 3.0))
        assertThat(p.toFloatArray()).isEqualTo(floatArrayOf(2.0f, 3.0f))
    }

    @Test
    fun immutableConversionsAndZero() {
        assertThat(KtImmutablePointD(KtImmutablePointD(1.0, 2.0))).isEqualTo(KtImmutablePointD(1.0, 2.0))
        assertThat(KtImmutablePointD.ZERO).isEqualTo(KtImmutablePointD(0.0, 0.0))
        val p = KtImmutablePointD(1.0, 2.0)
        assertThat(p.toMutable()).isEqualTo(KtPointD(1.0, 2.0))
        assertThat(p.toDoubleArray()).isEqualTo(doubleArrayOf(1.0, 2.0))
        assertThat(p.toFloatArray()).isEqualTo(floatArrayOf(1.0f, 2.0f))
        assertThat(p.toFloat()).isEqualTo(KtImmutablePointF(1.0f, 2.0f))
    }

    @Test
    fun mutableConstructorsNegateLengthAndSets() {
        assertThat(KtPointD()).isEqualTo(KtPointD(0.0, 0.0))
        assertThat(KtPointD(KtImmutablePointD(1.0, 2.0))).isEqualTo(KtPointD(1.0, 2.0))
        assertThat(KtPointD(KtImmutablePointF(1.5f, 2.5f))).isEqualTo(KtPointD(1.5, 2.5))
        val p = KtPointD(3.0, 4.0)
        assertThat(p.length()).isEqualTo(5.0)
        p.negate()
        assertThat(p).isEqualTo(KtPointD(-3.0, -4.0))
        p.set(KtImmutablePointD(7.0, 8.0))
        assertThat(p).isEqualTo(KtPointD(7.0, 8.0))
        p.set(KtImmutablePointF(1.0f, 2.0f))
        assertThat(p).isEqualTo(KtPointD(1.0, 2.0))
    }
}
