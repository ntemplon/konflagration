package com.nfa.konflagration.math

import org.apache.commons.math3.linear.*

/**
 * Created by nathan on 9/21/15.
 */
public fun Array<out DoubleArray>.toMatrix(): RealMatrix = MatrixUtils.createRealMatrix(this)

public inline fun doubleArray(size: Int, initializer: (Int) -> Double): DoubleArray {
    val array = DoubleArray(size)
    for (i in 0..(size - 1)) {
        array[i] = initializer(i)
    }
    return array
}

public fun DoubleArray.toVector(): RealVector = ArrayRealVector(this)

public fun RealMatrix.solve(rhs: RealVector): RealVector = LUDecomposition(this).solver.solve(rhs)

public fun List<Point>.toBSpline(degree: Int): Spline = BSpline.fromClampedPoints(this, degree)