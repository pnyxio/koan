package io.pnyx.koan.lang

internal class Stack<T> {
    private val l: MutableList<T> = mutableListOf()
    fun isEmpty() = l.isEmpty()
    val size = l.size
    fun push(item: T) = l.add(item)
    fun pop() : T = l.removeAt(l.size - 1)
    fun peek() : T? = l.last()
}
