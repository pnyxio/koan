package io.pnyx.koan.obj

actual interface Obj : JixSrc, MutableMap<String, Any?>, Iterable<Pair<String,Any?>> {
    actual fun store(key: String, value: Any?): Any?

    actual companion object {
        actual operator fun invoke(vararg entries: Pair<String, Any?>): Obj {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    actual val len: Int
    actual fun fetch(key: String): Any?
    actual fun del(key: String): Any?
}