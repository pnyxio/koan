package io.pnyx.koan.obj



external interface JsArr {
    val length: Int
    fun concat()
    fun copyWithin()
    fun entries()
    fun every()
    fun fill()
    fun filter()
    fun find()
    fun findIndex()
    fun flat()
    fun flatMap()
    fun forEach()
    fun includes()
    fun indexOf()
    fun join()
    fun keys()
    fun lastIndexOf()
    fun map()
    fun pop()
    fun push()
    fun reduce()
    fun reduceRight()
    fun reverse()
    fun shift()
    fun slice()
    fun some()
    fun sort()
    fun splice()
    fun toLocaleString()
    fun toSource()
    fun unshift()
    fun values()
    //TODO Array.prototype[@@iterator]()
}


actual interface Arr<T> : /*MutableList<T>,*/ JsArr, IArr<T> {
    override fun append(elm: T): T = elm.also { asDynamic().push(it) }

    override fun pos(index: Int): Any? = asDynamic()[index]

    override val len: Int get() = length

    actual companion object {

        actual operator fun <T> invoke(vararg elements: T): Arr<T> {
            val a = js("[]").unsafeCast<Arr<T>>()
            for(el in elements) {
                a.append(el)
            }
            return a
        }

        actual operator fun <T> invoke(): Arr<T> = js("[]").unsafeCast<Arr<T>>()
        actual fun ofAny(): Arr<Any?> = js("[]").unsafeCast<Arr<Any?>>()
    }

}


fun <T> Arr<T>.remove(element: T): Boolean {
    TODO("not implemented 1") //To change body of created functions use File | Settings | File Templates.
}

fun <T> JsArr.remove2(element: T): Boolean {
    kotlin.TODO("not implemented 2") //To change body of created functions use File | Settings | File Templates.
}
