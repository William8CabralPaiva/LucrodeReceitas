package com.cabral.arch.extensions

fun Float?.intNumber(): String {
    this?.let {
        if (this % 1 == 0f) {
            return this.toInt().toString()
        }
    }
    return this.toString()
}