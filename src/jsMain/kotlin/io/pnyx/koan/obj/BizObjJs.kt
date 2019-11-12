package io.pnyx.koan.obj

import io.pnyx.koan.lang.Object
import io.pnyx.koan.lang.ObjectConstructor

actual inline fun <reified T> biz(): T = biz<T>(Obj())

actual inline fun <reified T> biz(o: Obj): T {
    val t = o.asDynamic() as T
    Object.getPrototypeOf<dynamic, T>(t).constructor = ObjectConstructor
    return t
}
