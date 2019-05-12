package io.pnyx.koan.lang

actual typealias IoException = java.io.IOException

actual typealias SecurityException = java.lang.SecurityException

actual open class NotFoundException actual constructor(message: String?, cause: Throwable?) : IoException(message, cause)
