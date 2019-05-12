package io.pnyx.koan.obj

import io.pnyx.koan.lang.illegalState


interface JsonSerializable {
    fun toWire(): ByteArray
    fun toWireStr(): String
}

interface JixSrc {
    fun streamTo(handler: JixHandler)

    companion object {
        fun of(value: Any?): JixSrc = when (value) {
            null -> Null
            true -> True
            false -> False
            is Number -> NumberEv(value)
            is String -> StringEv(value)
            is Obj -> value
            is TArr<*> -> value
            else -> throw IllegalArgumentException()
        }
    }
}

interface JixEv: JixSrc {
    override fun streamTo(handler: JixHandler) = dispatchToHandler(this, handler)
}


interface PrimOrNull<T>: JixEv, JsonSerializable {
    val inner : T
    fun isNull() = inner == null

}

inline class StringEv(override val inner: String): PrimOrNull<String> {
    override fun toWire() = JsonPrimEncoder.toJsonBytes(inner)

    override fun toWireStr() = JsonPrimEncoder.toJsonString(inner)

    override fun streamTo(handler: JixHandler) = dispatchToHandler(this, handler)

}

inline class NumberEv(override val inner: Number): PrimOrNull<Number> {
    override fun toWire() = JsonPrimEncoder.toJsonBytes(inner)

    override fun toWireStr() = JsonPrimEncoder.toJsonString(inner)

    override fun streamTo(handler: JixHandler) = dispatchToHandler(this, handler)

}

inline class KeyEv(val inner: String): JixEv, JsonSerializable {
    override fun toWire() = JsonPrimEncoder.toJsonBytes(inner)

    override fun toWireStr() = JsonPrimEncoder.toJsonString(inner)

    override fun streamTo(handler: JixHandler) = dispatchToHandler(this, handler)

}

object Null : PrimOrNull<Any?> {
    override val inner: Any? = null

    override fun toWire()= NULL_ON_WIRE

    override fun toWireStr() = "null"

}

object True : PrimOrNull<Boolean> {
    override val inner: Boolean = true

    override fun toWire() = TRUE_ON_WIRE

    override fun toWireStr() = "true"

}

object False : PrimOrNull<Boolean> {
    override val inner: Boolean = false

    override fun toWire() = FALSE_ON_WIRE

    override fun toWireStr() = "false"

}

object StartObj: JixEv
object EndObj: JixEv

object StartArr: JixEv
object EndArr: JixEv

///////////////////////////


interface JixHandler {
    fun <T> prim(value: PrimOrNull<T>)
    fun key(key: KeyEv)
    fun startObj()
    fun endObj()
    fun startArr()
    fun endArr()

}

fun dispatchToHandler(ev: JixEv, h: JixHandler) = when(ev) {
    is PrimOrNull<*> -> h.prim(ev)
    is KeyEv -> h.key(ev)
    is StartObj -> h.startObj()
    is EndObj -> h.endObj()
    is StartArr -> h.startArr()
    is EndArr -> h.endArr()
    else -> illegalState()
}
