package io.pnyx.koan.lang

import kotlin.reflect.KClass

actual class Ctor<T:Any> actual constructor(val klass: KClass<T>) {
    actual operator fun invoke(vararg args: Any?): T {
        val jsCtor = klass.js
        return jsNew(jsCtor, *args)
    }
}
