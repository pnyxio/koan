package io.pnyx.koan.obj


expect interface Obj : JixSrc, Iterable<Pair<String,Any?>> {
    val len: Int

    fun store(key: String, value: Any?): Any?
    fun fetch(key: String): Any?
    fun del(key: String): Any?
    fun containsKey(key: String): Boolean

    companion object {
        operator fun invoke(vararg entries: Pair<String, Any?>): Obj// = ObjImpl(linkedMapOf(*entries))
    }

}

/*
//TODO should implement equals
interface Obj: MutableMap<String, Any?>, JixSrc {
    var biz: Biz?//TODO remove

    fun store(key: String, value: Any?): Any? {//TODO keep or remove ??
        this[key] = value
        return value
    }

    override fun streamTo(handler: JixHandler) {
        handler.startObj()
        for(entry in entries) {
            handler.key(KeyEv(entry.key))
            JixSrc.of(entry.value).streamTo(handler)
        }
        handler.endObj()
    }
    companion object {
        operator fun invoke(vararg entries: Pair<String, Any?>): Obj = ObjImpl(linkedMapOf(*entries))
    }
}

internal class ObjImpl(private val m: LinkedHashMap<String, Any?>): MutableMap<String, Any?> by m, Obj {
    override var biz: Biz? = null
}
*/

