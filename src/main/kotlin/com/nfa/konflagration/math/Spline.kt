package com.nfa.konflagration.math

/**
 * Created by nathan on 10/4/15.
 */
public interface Spline {
    public val domain: Interval
    public fun valueAt(t: Double): Point
}