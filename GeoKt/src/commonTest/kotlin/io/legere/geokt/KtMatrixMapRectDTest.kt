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
import kotlin.test.assertTrue

class KtMatrixMapRectDTest {
    @Test
    fun mapRectDoubleMatchesExactScaleTranslate() {
        // scale (2,3) then translate (10,20): rect (1,1,2,2) -> (12,23,14,26).
        val m = KtMatrix().apply { postScale(2.0, 3.0); postTranslate(10.0, 20.0) }
        val rect = KtRectD(1.0, 1.0, 2.0, 2.0)
        m.mapRect(rect)
        assertThat(rect).isEqualTo(KtRectD(12.0, 23.0, 14.0, 26.0))
    }

    @Test
    fun mapRectDoubleSrcDstOverload() {
        val m = KtMatrix().apply { postScale(2.0, 2.0) }
        val dst = KtRectD()
        m.mapRect(dst, KtImmutableRectD(1.0, 1.0, 3.0, 3.0))
        assertThat(dst).isEqualTo(KtRectD(2.0, 2.0, 6.0, 6.0))
    }

    @Test
    fun doubleMapRectKeepsPrecisionFloatLoses() {
        // A scale that isn't exactly representable in Float, on a large coordinate, so the Float path
        // (mapRect(KtRectF)) rounds while the double path (mapRect(KtRectD)) stays exact.
        val scale = 1.0 / 3.0
        val coord = 9_000_001.0
        val m = KtMatrix().apply { postScale(scale, scale) }

        val d = KtRectD(coord, coord, coord, coord)
        m.mapRect(d)
        val expected = coord * scale
        // Double path is exact to the model computation.
        assertThat(d.left).isEqualTo(expected)

        val f = KtRectF(coord.toFloat(), coord.toFloat(), coord.toFloat(), coord.toFloat())
        m.mapRect(f)
        // Float path diverges from the exact value (the precision the central model would lose).
        assertTrue(kotlin.math.abs(f.left - expected) > kotlin.math.abs(d.left - expected))
    }
}
