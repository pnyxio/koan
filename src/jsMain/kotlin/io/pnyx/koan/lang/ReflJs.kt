package io.pnyx.koan.lang

import kotlin.reflect.KClass

actual class Ctor<T:Any> actual constructor(val klass: KClass<T>) {
    actual operator fun invoke(vararg args: Any?): T {
        val jsCtor = klass.js
        //val o = js("args.unshift(null); new (Function.prototype.bind.apply(jsCtor, args));")
//        throw Exception(js("JSON.stringify(o);").toString())
        return jsNew(jsCtor, *args)
    }
}
