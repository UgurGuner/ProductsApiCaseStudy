package com.eugurguner.productsapicasestudy.core.extensions

fun Double.formatPrice(): String =
    try {
        toInt().formatPrice()
    } catch (ex: Throwable) {
        "0"
    }

fun Int.formatPrice(): String =
    try {
        toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    } catch (ex: Throwable) {
        "0"
    }