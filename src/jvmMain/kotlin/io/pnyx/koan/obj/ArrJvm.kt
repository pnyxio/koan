package io.pnyx.koan.obj





actual interface GArr<T>: MutableList<T>


class GArrImpl<T>(val l: MutableList<T>) : GArr<T>, MutableList<T> by l


actual interface GObj<K,T>: MutableMap<K,T> {

}










actual interface Miao<T> : MutableList<T>{
}
actual fun <T> Miao<T>.asList() : MutableList<T> = this


















actual typealias MyArr<T> = TArr<T>
