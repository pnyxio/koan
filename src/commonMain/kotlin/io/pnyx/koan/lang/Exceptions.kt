package io.pnyx.koan.lang

expect open class NotFoundException(message: String?, cause: Throwable?) : IoException

expect open class IoException(message: String?, cause: Throwable?): Exception

expect open class SecurityException(message: String?, cause: Throwable?): RuntimeException

inline fun <reified E: Exception> raise(message: String? = null, cause: Throwable? = null): Nothing = throw ctor<E>().invoke(message, cause)

fun illegalState(message: String? = null, cause: Throwable? = null): Nothing = raise<IllegalStateException>(message, cause)

fun illegalStateAssert(message: String? = null, cause: Throwable? = null, test: () -> Boolean) {
    if(! test()) raise<IllegalStateException>(message, cause)
}

fun <T> notNull(arg: T?, lazyMessage: (() -> String)? = null): T = arg ?: throw NullPointerException(lazyMessage?.invoke())
