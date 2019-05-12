package io.pnyx.koan.lang

import kotlin.math.floor

//eg. 1.0.toStringPretty() == "1"
fun Number.toStringPretty() = if (isInteger()) toLong().toString() else toString()


//eg. 1.0.isInteger() == true
fun Number.isInteger() = toDouble().let { it == floor(it) }

//fun String.isInteger(): Boolean {
//    try {
//        toLong()
//        return true
//    } catch (e: NumberFormatException) {
//        return false
//    }
//}
