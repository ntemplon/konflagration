package com.nfa.konflagration.math

/**
 * Created by nathan on 9/15/15.
 */
data class Point(public val x: Double, public val y: Double) {

    public fun distanceTo(other: Point): Double {
        val xDiff = this.x - other.x
        val yDiff = this.y - other.y
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff)
    }

    public fun toArray(): DoubleArray {
        return doubleArrayOf(this.x, this.y)
    }

}