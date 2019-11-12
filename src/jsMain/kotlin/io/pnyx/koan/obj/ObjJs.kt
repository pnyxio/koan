package io.pnyx.koan.obj

import io.pnyx.koan.lang.JsObject
import io.pnyx.koan.lang.Object
import io.pnyx.koan.lang.ObjectConstructor
import io.pnyx.koan.lang.copyDyn


actual interface Obj : JixSrc, JsObject, Iterable<Pair<String,Any?>> {
    actual fun <T:Any?> store(key: String, value: T): T

    actual companion object {
        actual operator fun invoke(vararg entries: Pair<String, Any?>): Obj {
            val t = js("{}")
            Object.getPrototypeOf<dynamic, Obj>(t).constructor = ObjectConstructor
            return t

        }
    }

    actual val len: Int
    actual fun fetch(key: String): Any?
    actual fun del(key: String): Any?
    actual fun existsKey(key: String): Boolean

}