package io.pnyx.koan.obj

import io.pnyx.koan.lang.JsArr


external interface JssArr<T>{
    //fun get(index: Int): T
    fun isEmpty(): Boolean
    fun first(): T
    fun add(element: T): Boolean
}

actual typealias Miao<T> = JssArr<T> //{
actual fun <T> Miao<T>.asList() : MutableList<T> = mutableListOf()
//    override operator fun get(index: Int): T {
//        return asDynamic()[index]
//    }
//    override fun add(element: T): Boolean {
//        asDynamic().push(element)
//        return true
//    }
//}
//val <T> JssArr<T>.size get() = asDynamic().length

fun <T> JssArr<T>.get(index: Int): T {
    return asDynamic()[index]
}

fun <T> JssArr<T>.isEmpty(): Boolean {
    return asDynamic().length == 0
}

fun <T> JssArr<T>.first(): T {
    return asDynamic()[0]
}




actual interface GArr<T>: MutableList<T>, JssArr<T> {

}




actual interface GObj<K,T>: MutableMap<K,T> {
    override val size: Int
        get() = asDynamic().length

}


















actual typealias MyArr<T> = JsArr<T>

