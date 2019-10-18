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
    override fun push2(elm: T): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pos(index: Int): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val len: Int get() = length

    actual companion object {

        actual operator fun <T> invoke(vararg elements: T): Arr<T> {
            val a = js("[]").unsafeCast<Arr<T>>()
            for(el in elements) {
                a.push2(el)
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


//actual interface Obj: MutableMap<String, Any?>, JsObj {
//    companion object {
//        fun make() : Obj {
//            return js("{}").unsafeCast<Obj>()
//        }
//    }
//
//}
//
////class Arrr<T> : Arr<T>
//
//external interface JssArr<T>{
//    //fun get(index: Int): T
//    fun isEmpty(): Boolean
//    fun first(): T
//    fun add(element: T): Boolean
//}
//
//actual typealias Miao<T> = JssArr<T> //{
//actual fun <T> Miao<T>.asList() : MutableList<T> = mutableListOf()
////    override operator fun get(index: Int): T {
////        return asDynamic()[index]
////    }
////    override fun add(element: T): Boolean {
////        asDynamic().push(element)
////        return true
////    }
////}
////val <T> JssArr<T>.size get() = asDynamic().length
//
//fun <T> JssArr<T>.get(index: Int): T {
//    return asDynamic()[index]
//}
//
//fun <T> JssArr<T>.isEmpty(): Boolean {
//    return asDynamic().length == 0
//}
//
//fun <T> JssArr<T>.first(): T {
//    return asDynamic()[0]
//}
//
//
//
//
//actual interface GArr<T>: MutableList<T>, JssArr<T> {
//
//}
//
//
//
//
//actual interface GObj<K,T>: MutableMap<K,T> {
//    override val size: Int
//        get() = asDynamic().length
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//actual typealias MyArr<T> = JsArr<T>
//
