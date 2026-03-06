package io.legere.geokt

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.show
import kotlin.math.abs

/**
 * Asserts that the actual Double value is close to the expected value within a given tolerance.
 *
 * @param expected The expected value.
 * @param tolerance The maximum allowed difference between the actual and expected values.
 */
fun Assert<Double>.isCloseTo(expected: Double, tolerance: Double) {
    given { actual ->
        if (abs(actual - expected) <= tolerance) {
            return
        }
        expected("to be close to ${show(expected)} with tolerance ${show(tolerance)} but was ${show(actual)}")
    }
}
