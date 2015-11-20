package com.nfa.konflagration.math

/**
 * Created by nathan on 10/4/15.
 */
data class Interval(public val start: Double, public val end: Double) {
    public operator fun contains(value: Double) = start <= value && end >= value
}