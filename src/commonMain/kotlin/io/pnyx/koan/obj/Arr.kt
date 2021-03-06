@file:Suppress("UNCHECKED_CAST")

package io.pnyx.koan.obj


interface IArr<T> : JixSrc, Iterable<T> {
    val len: Int
    fun append(elm: T): T

    override fun streamTo(handler: JixHandler) {
        handler.startArr()
        for(elm in this) {
            JixSrc.of(elm).streamTo(handler)
        }
        handler.endArr()
    }

    fun pos(index: Int): Any?

}

expect interface Arr<T> : IArr<T> {
    companion object {
        operator fun <T> invoke(vararg elements: T): Arr<T>

        operator fun <T> invoke(): Arr<T>

        fun ofAny(): Arr<Any?>

    }

}

typealias AnyArr = Arr<Any?>



