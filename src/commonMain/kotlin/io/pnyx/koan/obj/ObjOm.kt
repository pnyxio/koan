package io.pnyx.koan.obj

enum class JsonType {
    obj, arr, num, str, nil, bool
}

fun Any.isPrim(): Boolean = when(this) {
    is String, is Boolean, is Number -> true
    else -> false
}

//fun Any?.isPrimOrNull(): Boolean = when(this) {
//    null -> true
//    is String -> true
//    is Boolean -> true
//    is Number -> true
//    else -> false
//}

//fun <T> T.asPrimitive(): T = when(this) {
//    is String -> this
//    is Boolean -> this
//    is Number -> this
//    else -> throw IllegalArgumentException()
//}

//inline fun <T> T?.asPrimitiveOrNull(): T? = when(this) {
//    null -> null
//    is String -> this
//    is Boolean -> this
//    is Number -> this
//    else -> throw IllegalArgumentException()
//}





