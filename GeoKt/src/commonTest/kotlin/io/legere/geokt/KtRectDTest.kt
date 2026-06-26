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
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class KtRectDTest {
    @Test
    fun defaultConstructorIsEmpty() {
        assertThat(KtImmutableRectD().isEmpty).isTrue()
        assertThat(KtRectD().isEmpty).isTrue()
    }

    @Test
    fun widensFromFloatAndInt() {
        assertThat(KtImmutableRectD(KtImmutableRectF(1.5f, 2.5f, 3.5f, 4.5f)))
            .isEqualTo(KtImmutableRectD(1.5, 2.5, 3.5, 4.5))
        assertThat(KtImmutableRectD(KtImmutableRect(1, 2, 3, 4)))
            .isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))
    }

    @Test
    fun widthHeightCenter() {
        val r = KtImmutableRectD(10.0, 20.0, 40.0, 60.0)
        assertThat(r.width).isEqualTo(30.0)
        assertThat(r.height).isEqualTo(40.0)
        assertThat(r.centerX).isEqualTo(25.0)
        assertThat(r.centerY).isEqualTo(40.0)
    }

    @Test
    fun intersectsAndContains() {
        val a = KtImmutableRectD(0.0, 0.0, 10.0, 10.0)
        assertThat(a.intersects(KtImmutableRectD(5.0, 5.0, 15.0, 15.0))).isTrue()
        assertThat(a.intersects(KtImmutableRectD(20.0, 20.0, 30.0, 30.0))).isFalse()
        assertThat(a.contains(5.0, 5.0)).isTrue()
        assertThat(a.contains(KtImmutableRectD(2.0, 2.0, 8.0, 8.0))).isTrue()
    }

    @Test
    fun intersect4ArgUsesFieldsNotParams() {
        // Regression vs the KtRectF bug where max(left, left) made this a no-op (params shadowed fields).
        val r = KtImmutableRectD(0.0, 0.0, 10.0, 10.0).intersect(2.0, 3.0, 8.0, 7.0)
        assertThat(r).isEqualTo(KtImmutableRectD(2.0, 3.0, 8.0, 7.0))
    }

    @Test
    fun inset4ArgInsetsBottomByBottomNotRight() {
        // Regression vs the KtRectF bug where the bottom inset used the `right` param.
        val r = KtImmutableRectD(0.0, 0.0, 100.0, 100.0).inset(10.0, 20.0, 30.0, 40.0)
        assertThat(r).isEqualTo(KtImmutableRectD(10.0, 20.0, 70.0, 60.0))

        val m = KtRectD(0.0, 0.0, 100.0, 100.0)
        m.inset(10.0, 20.0, 30.0, 40.0)
        assertThat(m).isEqualTo(KtRectD(10.0, 20.0, 70.0, 60.0))
    }

    @Test
    fun unionOffsetSort() {
        assertThat(KtImmutableRectD(0.0, 0.0, 10.0, 10.0).union(KtImmutableRectD(5.0, 5.0, 20.0, 20.0)))
            .isEqualTo(KtImmutableRectD(0.0, 0.0, 20.0, 20.0))
        assertThat(KtImmutableRectD(0.0, 0.0, 10.0, 10.0).offset(5.0, 5.0))
            .isEqualTo(KtImmutableRectD(5.0, 5.0, 15.0, 15.0))
        assertThat(KtImmutableRectD(10.0, 10.0, 0.0, 0.0).sort())
            .isEqualTo(KtImmutableRectD(0.0, 0.0, 10.0, 10.0))
    }

    @Test
    fun mutableSetAndToImmutable() {
        val m = KtRectD()
        m.set(1.0, 2.0, 3.0, 4.0)
        assertThat(m.toImmutable()).isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))
        m.set(KtImmutableRectF(5.5f, 6.5f, 7.5f, 8.5f))
        assertThat(m).isEqualTo(KtRectD(5.5, 6.5, 7.5, 8.5))
    }

    @Test
    fun boundaryNarrowing() {
        val r = KtImmutableRectD(1.0, 2.0, 3.0, 4.0)
        assertThat(r.toFloat()).isEqualTo(KtImmutableRectF(1.0f, 2.0f, 3.0f, 4.0f))
        assertThat(r.toDoubleArray()).isEqualTo(doubleArrayOf(1.0, 2.0, 3.0, 4.0))
        assertThat(r.toFloatArray()).isEqualTo(floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f))
    }

    @Test
    fun immutableConstructorsAndEmpty() {
        assertThat(KtImmutableRectD(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))).isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))
        assertThat(KtImmutableRectD.EMPTY.isEmpty).isTrue()
        assertThat(KtImmutableRectD(1.0, 2.0, 3.0, 4.0).setEmpty()).isEqualTo(KtImmutableRectD.EMPTY)
        assertThat(KtImmutableRectD(1.0, 2.0, 3.0, 4.0).toMutable()).isEqualTo(KtRectD(1.0, 2.0, 3.0, 4.0))
    }

    @Test
    fun immutableInsetIntersectUnionVariants() {
        val r = KtImmutableRectD(0.0, 0.0, 100.0, 100.0)
        assertThat(r.inset(10.0, 20.0)).isEqualTo(KtImmutableRectD(10.0, 20.0, 90.0, 80.0))
        assertThat(r.inset(0.0, 0.0)).isEqualTo(r) // identity fast-path
        assertThat(r.intersect(KtImmutableRectD(50.0, 50.0, 150.0, 150.0)))
            .isEqualTo(KtImmutableRectD(50.0, 50.0, 100.0, 100.0))
        assertThat(r.union(120.0, 120.0)).isEqualTo(KtImmutableRectD(0.0, 0.0, 120.0, 120.0)) // union point
        assertThat(r.union(10.0, 10.0, 120.0, 120.0)).isEqualTo(KtImmutableRectD(0.0, 0.0, 120.0, 120.0))
        // union onto an empty rect adopts the other
        assertThat(KtImmutableRectD().union(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))).isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))
        assertThat(KtImmutableRectD().union(5.0, 6.0)).isEqualTo(KtImmutableRectD())
    }

    @Test
    fun immutableOffsetToRoundOut() {
        assertThat(KtImmutableRectD(0.0, 0.0, 10.0, 20.0).offsetTo(5.0, 5.0))
            .isEqualTo(KtImmutableRectD(5.0, 5.0, 15.0, 25.0))
        assertThat(KtImmutableRectD(1.2, 1.8, 3.4, 4.6).roundOut()).isEqualTo(KtImmutableRect(1, 1, 4, 5))
    }

    @Test
    fun baseRectAccessorsAndContains() {
        val r = KtRectD(0.0, 0.0, 10.0, 10.0)
        assertThat(r.width()).isEqualTo(10.0)
        assertThat(r.height()).isEqualTo(10.0)
        assertThat(r.centerX()).isEqualTo(5.0)
        assertThat(r.centerY()).isEqualTo(5.0)
        assertThat(r.isEmpty()).isFalse()
        assertThat(r.contains(2.0, 2.0, 8.0, 8.0)).isTrue()
        assertThat(r.contains(KtImmutableRect(2, 2, 8, 8))).isTrue() // contains(IntRectValues)
        assertThat(r.intersects(5.0, 5.0, 15.0, 15.0)).isTrue() // 4-arg intersects
        val dstRound = KtRect()
        KtImmutableRectD(1.4, 1.6, 3.5, 4.4).round(dstRound)
        assertThat(dstRound).isEqualTo(KtRect(1, 2, 4, 4))
        val dstRoundOut = KtRect()
        KtImmutableRectD(1.4, 1.6, 3.5, 4.4).roundOut(dstRoundOut)
        assertThat(dstRoundOut).isEqualTo(KtRect(1, 1, 4, 5))
    }

    @Test
    fun mutableAllOps() {
        val m = KtRectD(KtImmutableRectD(0.0, 0.0, 100.0, 100.0))
        m.inset(10.0, 20.0)
        assertThat(m).isEqualTo(KtRectD(10.0, 20.0, 90.0, 80.0))
        m.set(0.0, 0.0, 100.0, 100.0)
        m.intersect(50.0, 50.0, 150.0, 150.0)
        assertThat(m).isEqualTo(KtRectD(50.0, 50.0, 100.0, 100.0))
        m.set(0.0, 0.0, 10.0, 10.0)
        m.intersect(KtImmutableRectD(5.0, 5.0, 15.0, 15.0)) // intersect(other) body (non-empty)
        assertThat(m).isEqualTo(KtRectD(5.0, 5.0, 10.0, 10.0))
        m.set(0.0, 0.0, 10.0, 10.0)
        m.union(KtImmutableRectD(5.0, 5.0, 20.0, 20.0))
        assertThat(m).isEqualTo(KtRectD(0.0, 0.0, 20.0, 20.0))
        m.set(0.0, 0.0, 10.0, 10.0)
        m.union(20.0, 20.0)
        assertThat(m).isEqualTo(KtRectD(0.0, 0.0, 20.0, 20.0))
        m.set(0.0, 0.0, 10.0, 10.0)
        m.union(5.0, 5.0, 20.0, 20.0)
        assertThat(m).isEqualTo(KtRectD(0.0, 0.0, 20.0, 20.0))
        m.offset(5.0, 5.0)
        assertThat(m).isEqualTo(KtRectD(5.0, 5.0, 25.0, 25.0))
        m.offsetTo(0.0, 0.0)
        assertThat(m).isEqualTo(KtRectD(0.0, 0.0, 20.0, 20.0))
        m.set(KtImmutableRect(10, 10, 0, 0)) // IntRectValues set
        m.sort()
        assertThat(m).isEqualTo(KtRectD(0.0, 0.0, 10.0, 10.0))
        assertThat(m.intersects(KtImmutableRectD(5.0, 5.0, 15.0, 15.0))).isTrue()
        assertThat(m.roundOut()).isEqualTo(KtRect(0, 0, 10, 10))
        m.setEmpty()
        assertThat(m.isEmpty).isTrue()
    }

    @Test
    fun emptyAndEdgeBranches() {
        assertThat(KtRectD.EMPTY.isEmpty).isTrue() // companion EMPTY
        assertThat(KtRectD(KtImmutableRectF(1.5f, 2.5f, 3.5f, 4.5f))).isEqualTo(KtRectD(1.5, 2.5, 3.5, 4.5))

        // union(other) onto an empty immutable, where `other` isn't a KtImmutableRectD (the ?: fallback).
        assertThat(KtImmutableRectD().union(KtRectD(1.0, 2.0, 3.0, 4.0)))
            .isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))
        // union(other) where other is empty -> unchanged.
        assertThat(KtImmutableRectD(1.0, 2.0, 3.0, 4.0).union(KtImmutableRectD()))
            .isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))
        // union(4-arg) onto empty this -> adopts the args.
        assertThat(KtImmutableRectD().union(1.0, 2.0, 3.0, 4.0)).isEqualTo(KtImmutableRectD(1.0, 2.0, 3.0, 4.0))

        // Mutable empty-branch coverage.
        val m = KtRectD()
        m.union(KtImmutableRectD()) // other empty -> no-op
        assertThat(m.isEmpty).isTrue()
        m.union(KtImmutableRectD(1.0, 2.0, 3.0, 4.0)) // this empty -> adopt
        assertThat(m).isEqualTo(KtRectD(1.0, 2.0, 3.0, 4.0))
        // 4-arg union and (x,y) union onto an empty mutable rect (the this.isEmpty branches).
        val e1 = KtRectD()
        e1.union(1.0, 2.0, 3.0, 4.0)
        assertThat(e1).isEqualTo(KtRectD(1.0, 2.0, 3.0, 4.0))
        val e2 = KtRectD()
        e2.union(5.0, 6.0) // this empty -> no-op return
        assertThat(e2.isEmpty).isTrue()
        val empty = KtRectD()
        empty.intersect(KtImmutableRectD(1.0, 2.0, 3.0, 4.0)) // this empty -> return
        assertThat(empty.isEmpty).isTrue()
        empty.intersect(1.0, 2.0, 3.0, 4.0) // 4-arg, this empty -> return
        assertThat(empty.isEmpty).isTrue()
        KtRectD(0.0, 0.0, 10.0, 10.0).intersect(KtImmutableRectD()) // other empty -> return
    }

    @Test
    fun mutableBoundaryAndConstructors() {
        assertThat(KtRectD(KtImmutableRect(1, 2, 3, 4))).isEqualTo(KtRectD(1.0, 2.0, 3.0, 4.0))
        val m = KtRectD(1.0, 2.0, 3.0, 4.0)
        assertThat(m.toFloat()).isEqualTo(KtImmutableRectF(1.0f, 2.0f, 3.0f, 4.0f))
        assertThat(m.toDoubleArray()).isEqualTo(doubleArrayOf(1.0, 2.0, 3.0, 4.0))
        assertThat(m.toFloatArray()).isEqualTo(floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f))
        m.set(KtImmutableRectD(9.0, 9.0, 9.0, 9.0)) // DoubleRectValues set
        assertThat(m.left).isEqualTo(9.0)
    }
}
