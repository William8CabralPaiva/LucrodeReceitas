package com.cabral.arch.extensions

import java.math.RoundingMode

fun Float?.removeEndZero(): String {
    this?.let {
        if (this % 1 == 0f) {
            return toInt().toString()
        }
    }
    return toString()
}

fun Float?.roundingNumber(scale: Int = 2): String {
    this?.let {
        return toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
    }
    return toString()
}