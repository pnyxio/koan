package io.pnyx.koan.obj

import kotlin.collections.ArrayList


actual interface Arr<T> : IArr<T>, MutableList<T> {
    override fun pos(index: Int): Any? = get(index)

    override val len: Int get() = size

    override fun append(elm: T): T {
        add(elm)
        return elm
    }

    actual companion object {
        actual operator fun <T> invoke(vararg elements: T): Arr<T>  = ArrImpl(arrayListOf(*elements))

        actual operator fun <T> invoke(): Arr<T> = ArrImpl()

        actual fun ofAny(): Arr<Any?> = ArrImpl()

    }

}

class ArrImpl<T>(private val l: MutableList<T> = ArrayList() ): MutableList<T> by l, Arr<T> {
}
