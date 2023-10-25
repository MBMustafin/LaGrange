package ru.smak.math

import kotlin.math.abs
import kotlin.math.max

infix fun Double.eq(other: Double) =
    abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 10.0
fun Double.eq(other: Double, exponent: Double) =
    abs(this - other) < Math.ulp(abs(this) + abs(other) + abs(exponent)) * 10.0
infix fun Double.neq(other: Double) = !(this eq other)
fun Double.neq(other: Double, exponent: Double) = !(eq(other, exponent))
infix fun Double.leq(other: Double) = this < other || eq(other)
fun Double.leq(other: Double, exponent: Double) = this < other || eq(other)
infix fun Double.geq(other: Double) = this > other || abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 10.1
fun Double.geq(other: Double, exponent: Double) = this > other || eq(other)
infix fun Double.lt(other: Double) = this < other && abs(this - other) >= max(Math.ulp(this), Math.ulp(other)) * 10.1
fun Double.lt(other: Double, exponent: Double) = this < other && neq(other)
infix fun Double.gt(other: Double) = this > other && abs(this - other) >= max(Math.ulp(this), Math.ulp(other)) * 10.1
fun Double.gt(other: Double, exponent: Double) = this > other && neq(other)