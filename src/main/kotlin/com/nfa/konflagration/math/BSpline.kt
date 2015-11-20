package com.nfa.konflagration.math

import org.apache.commons.math3.linear.LUDecomposition

/**
 * A class representing a clamped B-Spline of arbitrary order.
 * Except at the end points, the spline is differentiable degree - 1 times
 *
 * @param knots the knots of the spline
 * @property degree The degree of the spline
 * @property domain the domain of the parameterization variable for the spline
 * @property knots the knots that form the knot vector of the spline
 * @property points the points that the spline passes through, in order
 * @constructor the constructor called by the companion creator methods
 */
public class BSpline private constructor(knots: Array<Double>, public val points: List<Point>, public override val domain: Interval) : Spline {

    // Properties
    private val pointsMatrix = points.map { doubleArrayOf(it.x, it.y) }.toTypedArray().toMatrix()
    private val knotsArray = knots
    private val n: Int = points.size - 1
    private val m: Int = knots.size - 1

    public val knots: List<Double> = knots.toList()
    public val degree = m - n - 1


    // Public Methods
    /**
     * Computes the value of the spline at the given value of t.
     *
     * @param t the value of the parameterization variable
     * @return the point that the spline passes through at the given value of t.
     */
    public override fun valueAt(t: Double): Point {
        assert(t in this.domain)

        val k = this.knotFor(t)
        val result = arrayOf(getNCoefficients(p = this.degree, knots = this.knotsArray, u = t, k = k)).toMatrix() * this.pointsMatrix
        return Point(result.getEntry(0, 0), result.getEntry(0, 1))
    }


    // Private Methods
    private fun knotFor(u: Double): Int {
        //this.knots.withIndex().last { value -> value.value <= u }.index
        // Special Cases
        if (u == this.knots[0]) {
            return 0
        } else if (u == this.knots[this.knots.size - 1]) {
            return this.knots.size - 1
        }

        // General Binary Search
        var left = 0
        var right = this.knots.size - 1
        while (left <= right) {
            val mid = (right + left) / 2
            val value = this.knots[mid]
            if (value <= u) {
                if (mid == this.knots.size - 1 || u < this.knots[mid + 1]) {
                    return mid
                } else {
                    left = mid + 1
                }
            } else {
                right = mid - 1
            }
        }
        return -1
    }


    companion object {

        // Public Methods
        /**
         * B-Spline algorithm based on http://www.cs.mtu.edu/~shene/COURSES/cs3621/NOTES/INT-APP/CURVE-INT-global.html
         *
         * @param points the points to spline
         * @param degree the degree of the resulting spline
         * @return a spline going through the points provided with the given degree
         */
        public fun fromClampedPoints(points: List<Point>, degree: Int): BSpline {
            // n is the last index
            val n = points.size - 1
            assert(degree <= n)

            // m is the number of knots
            val m = n + degree + 1

            // Chord Length spacing
            val t = getParamsChordLength(points)

            // Calculate knot vector
            val knots = Array(m + 1, { i ->
                if (i <= degree) {
                    0.0
                } else if (i >= m - degree) {
                    1.0
                } else {
                    val j = i - degree
                    (j..(j + degree - 1)).map { index -> t[index] }.sum() / degree;
                }
            })

            // Calculate the matrix of points, called D in the website
            val pointsMatrix = points.map { it.toArray() }.toTypedArray().toMatrix()

            // Calculate n
            val nCoefficients = t.map { getNCoefficients(degree, knots, it) }.toTypedArray().toMatrix()

            val controlPointMatrix = LUDecomposition(nCoefficients).solver.inverse * pointsMatrix
            val controlPoints = (0..controlPointMatrix.rowDimension - 1).map { i -> Point(controlPointMatrix.getEntry(i, 0), controlPointMatrix.getEntry(i, 1)) }

            return BSpline(knots, controlPoints, Interval(0.0, 1.0))
        }


        // Private Methods
        /**
         * Calculates parameters for the given points based on the distance between adjacent points
         *
         * @param points the points to assign parameters to
         * @return an array of parameters, from 0 to 1, spaced by chord length
         */
        private fun getParamsChordLength(points: List<Point>): DoubleArray {
            val distanceTo = DoubleArray(points.size)
            for (i in 1..(points.size - 1)) {
                distanceTo[i] = distanceTo[i - 1] + points[i].distanceTo(points[i - 1])
            }
            val totalDistance = distanceTo[distanceTo.size - 1]

            val params = DoubleArray(points.size)
            for (i in params.indices) {
                params[i] = if (i == 0) {
                    0.0
                } else if (i == points.size - 1) {
                    1.0
                } else {
                    distanceTo[i] / totalDistance
                }
            }
            return params
        }

        private fun getNCoefficients(p: Int, knots: Array<Double>, u: Double): DoubleArray {
            val m = knots.size - 1
            val n = m - p - 1

            // Special Cases
            if (u == knots[0]) {
                val coef = DoubleArray(n + 1)
                coef[0] = 1.0
                return coef
            } else if (u == knots[m]) {
                val coef = DoubleArray(n + 1)
                coef[n] = 1.0
                return coef
            }

            // Calculate the knot span
            val k = knots.withIndex().last { value ->
                value.value <= u
            }.index

            return getNCoefficients(p, knots, u, k)
        }

        private fun getNCoefficients(p: Int, knots: Array<Double>, u: Double, k: Int): DoubleArray {
            val m = knots.size - 1
            val n = m - p - 1
            val coef = DoubleArray(n + 1)

            // Special Cases
            if (k == 0) {
                coef[0] = 1.0
                return coef
            } else if (k == m) {
                coef[n] = 1.0
                return coef
            }

            coef[k] = 1.0

            for (d in 1..p) {
                coef[k - d] = ((knots[k + 1] - u) / (knots[k + 1] - knots[k - d + 1])) * coef[k - d + 1]
                for (i in (k - d + 1)..(k - 1)) {
                    coef[i] = ((u - knots[i]) / (knots[i + d] - knots[i])) * coef[i] + ((knots[i + d + 1] - u) / (knots[i + d + 1] - knots[i + 1])) * coef[i + 1]
                }
                coef[k] = ((u - knots[k]) / (knots[k + d] - knots[k])) * coef[k]
            }
            return coef
        }

    }

}