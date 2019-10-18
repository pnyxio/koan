package io.pnyx.koan.obj

import kotlin.collections.ArrayList


actual interface Arr<T> : IArr<T>, MutableList<T> {
    override fun pos(index: Int): Any? = get(index)

    override val len: Int get() = size

    override fun push2(elm: T): T {
        add(elm)
        return elm
    }

    actual companion object {
        actual operator fun <T> invoke(vararg elements: T): Arr<T> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        actual operator fun <T> invoke(): Arr<T> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        actual fun ofAny(): Arr<Any?> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}

class ArrImpl<T>(private val l: MutableList<T> /*= ArrayList<T>()*/): MutableList<T> by l, Arr<T> {

}
