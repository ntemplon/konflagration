package com.nfa.konflagration.math

import org.apache.commons.math3.linear.RealMatrix

/**
 * Created by nathan on 10/4/15.
 */
public operator fun RealMatrix.times(other: RealMatrix): RealMatrix = this.multiply(other)