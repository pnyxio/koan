@file:Suppress("UNCHECKED_CAST")

package io.pnyx.koan.obj

import kotlin.reflect.KClass
import kotlin.reflect.KProperty



expect interface GArr<T>: MutableList<T>
expect interface Miao<T> {

}
expect fun <T> Miao<T>.asList() : MutableList<T>

interface Bau<T> : Miao<T>

expect interface GObj<K,T>: MutableMap<K,T>








typealias Arr = TArr<Any?>//TODO !!!!!!!!!

val Any?.asArr: Arr get() = this as Arr




interface TArr<T>: MutableList<T>, JixSrc {
    fun push(elm: T): T/* {
        add(elm)
        return elm
    }*/

    var biz: BizList<*>?//TODO owner or mv to impl.... or.... whatever but break dep

    override fun streamTo(handler: JixHandler) {
        handler.startArr()
        for(elm in this) {
            JixSrc.of(elm).streamTo(handler)
        }
        handler.endArr()
    }

    companion object {
        inline operator fun <reified T> invoke(vararg elements: T): TArr<T> {
            val arr = ArrImpl<T>(ArrayList())
            arr.addAll(elements)
            return arr
        }

        operator fun invoke(): Arr = ArrImpl(ArrayList())

    }
}

class ArrImpl<T>(private val l: MutableList<T>): MutableList<T> by l, TArr<T> {
    override fun push(elm: T): T {
        add(elm)
        return elm
    }

    override var biz: BizList<*>? = null// = BizListImpl<>(this as TArr<Any?>)
}

