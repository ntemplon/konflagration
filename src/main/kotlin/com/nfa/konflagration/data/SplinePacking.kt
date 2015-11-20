package com.nfa.konflagration.data

import com.nfa.konflagration.math.Spline
import org.jfree.data.xy.XYSeries

/**
 * Converts a spline to a XYSeries for use in a JFreeChart.
 *
 * @param numPoints the number of points to create
 * @param name the name of the created series
 */
public fun Spline.toSeries(numPoints: Int = 100, name: String = "Default"): XYSeries {
    val data = XYSeries(name, false, true) // The parameter list is key, autoSort, allowDuplicateX
    val range = this.domain.end - this.domain.start
    val delta = range / (numPoints - 1.0)

    for (i in 0..numPoints) {
        val t = this.domain.start + delta * i
        val point = this.valueAt(t)
        data.add(point.x, point.y)
    }

    return data
}