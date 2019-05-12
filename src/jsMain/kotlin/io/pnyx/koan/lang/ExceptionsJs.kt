package io.pnyx.koan.lang

actual open class IoException actual constructor(message: String?, cause: Throwable?) : Exception(message, cause)

actual open class SecurityException actual constructor(message: String?, cause: Throwable?) : RuntimeException(message, cause)

actual open class NotFoundException actual constructor(message: String?, cause: Throwable?) : IoException(message, cause)
