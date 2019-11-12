package io.pnyx.koan.obj

import java.lang.reflect.Proxy
import kotlin.collections.LinkedHashMap

//TODO !!!! copy on write

actual interface Obj : JixSrc, MutableMap<String, Any?>, Iterable<Pair<String,Any?>> {
    actual val len: Int
    actual fun fetch(key: String): Any?
    actual fun <T:Any?> store(key: String, value: T): T
    actual fun del(key: String): Any?
    actual fun existsKey(key: String): Boolean

    actual companion object {
        actual operator fun invoke(vararg entries: Pair<String, Any?>): Obj {
            val o = ObjImpl()
            for (entry in entries) {
                o.put(entry.first, entry.second)
            }
            return o
        }
    }


}

class ObjImpl(private val m : MutableMap<String, Any?> = LinkedHashMap()) : MutableMap<String, Any?> by m, Obj {
    var proxy: Proxy? = null

    override fun <T:Any?> store(key: String, value: T): T {
        put(key, value)
        return value
    }

    override fun existsKey(key: String): Boolean = containsKey(key)

    override val len: Int
        get() = size

    override fun fetch(key: String): Any? = get(key)

    override fun del(key: String): Any? = remove(key)

    override fun streamTo(handler: JixHandler) {
        handler.startObj()
        for(entry in entries) {
            handler.key(KeyEv(entry.key))
            JixSrc.of(entry.value).streamTo(handler)
        }
        handler.endObj()
    }

    override fun iterator(): Iterator<Pair<String, Any?>> {
        val entriesItr = m.entries.iterator()
        return object : Iterator<Pair<String, Any?>> {
            override fun hasNext(): Boolean = entriesItr.hasNext()

            override fun next(): Pair<String, Any?> {
                val (k, v) = entriesItr.next()
                return k to v
            }

        }
    }

}