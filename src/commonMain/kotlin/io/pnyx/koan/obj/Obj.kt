package io.pnyx.koan.obj


expect interface Obj : JixSrc, Iterable<Pair<String,Any?>> {
    val len: Int

    fun <T: Any?> store(key: String, value: T): T
    fun fetch(key: String): Any?
    fun del(key: String): Any?
    fun existsKey(key: String): Boolean

    companion object {
        operator fun invoke(vararg entries: Pair<String, Any?>): Obj// = ObjImpl(linkedMapOf(*entries))
    }

}

