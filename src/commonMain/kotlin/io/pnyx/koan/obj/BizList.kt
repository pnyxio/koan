package io.pnyx.koan.obj

import kotlin.reflect.KClass
import kotlin.reflect.KProperty


/*

inline fun <reified T: Biz> A(): ArrProperty<T> = BizArrProperty<T>(T::class)
fun <T: Any> AP(): ArrProperty<T> = PrimArrProperty()
inline fun <reified T: Biz> AA(): ArrPropertyNonNull<T> = BizArrPropertyNonNull<T>(T::class)
fun <T: Any> AAP(): ArrPropertyNonNull<T> = PrimArrPropertyNonNull<T>()

fun <T> bizListOf(vararg elements: T) : BizList<T> {
    val l = ForwardingBizListImpl<T>()
    elements.forEach { l.add(it) }
    return l
}

//internal inline fun <reified T: Biz> reflBizListOf(vararg elements: T) : BizList<T> {
//    val l = WrappingBizListImpl<T>(wrapperOf(T::class))
//    l.addAll(elements)
//    return l
//
//}
interface BizList<T> : MutableList<T?> {
    val arr: Arr
    companion object {
        operator fun <T> invoke(): BizList<T> = ForwardingBizListImpl()
        inline fun <reified T: Biz> refl(): BizList<T> = WrappingBizListImpl(wrapperOf(T::class))
    }
}

open class WrappingBizListImpl<T>constructor(var innerArr: Arr, val wrapper: BizWrapFun<T>?): AbstractMutableList<T?>(), BizList<T> {
    constructor(wrapper: BizWrapFun<T>?) : this(TArr(), wrapper)
    override val arr: Arr get() = innerArr
    init {
        require(innerArr.biz == null)
        innerArr.biz = this
    }
    override val size: Int get() = arr.size
    override fun get(index: Int): T? = anyToT<T>(arr[index], wrapper)

    override fun clear() = arr.clear()
    override fun removeAt(index: Int): T? = anyToT<T>(arr.removeAt(index), wrapper)

    override fun set(index: Int, element: T?): T? {
        arr.set(index, tToObjOm<T>(element))
        return element
    }
    override fun add(index: Int, element: T?) {
        arr.add(index, tToObjOm<T>(element))
    }

}

class ForwardingBizListImpl<T>constructor(innerArr: Arr): WrappingBizListImpl<T>(innerArr, null) {
    constructor() : this(TArr())
}

@Suppress("UNCHECKED_CAST")
internal fun <T> anyToT(a: Any?, wrapper: BizWrapFun<T>?): T? = when {
    a == null -> null
    a.isPrim() -> a as T
    a is Obj -> a.biz as T? ?: wrapper!!(a)
    else -> throw IllegalStateException()
}

internal fun <T> tToObjOm(t: T?): Any? = when {
    t == null -> null
    t.isPrim() -> t
    t is Biz -> t.obj
    else -> throw IllegalStateException()
}

@Suppress("UNCHECKED_CAST")
open class ArrProperty<T>(val wrapper: BizWrapFun<T>?) {
    operator fun getValue(thisRef: Biz, property: KProperty<*>): BizList<T?>? {
        val x = thisRef.obj[property.name]
        return when {
            x is TArr<*> -> x.biz as BizList<T?>? ?: WrappingBizListImpl<T?>(x as Arr, wrapper)
            x == null -> null
            else -> throw IllegalStateException()
        }
    }

    operator fun setValue(thisRef: Biz, property: KProperty<*>, value: BizList<*>?) {
        thisRef.obj.put(property.name, value?.arr)
    }
}

class BizArrProperty<T: Biz>(typeParameter: KClass<T>): ArrProperty<T>(wrapperOf(typeParameter))

class PrimArrProperty<T: Any>() : ArrProperty<T>(null)


@Suppress("UNCHECKED_CAST")
open class ArrPropertyNonNull<T>(val wrapper: BizWrapFun<T>?) {
    operator fun getValue(thisRef: Biz, property: KProperty<*>): BizList<T?> {
        val x = thisRef.obj[property.name]
        return when {
            x is TArr<*> -> x.biz as BizList<T?>? ?: WrappingBizListImpl<T?>(x as Arr, wrapper)
            x == null -> {
                val l = WrappingBizListImpl<T?>(wrapper)
                thisRef.obj[property.name] = l.arr
                return l
            }
            else -> throw IllegalStateException()
        }
    }

    operator fun setValue(thisRef: Biz, property: KProperty<*>, value: BizList<*>?) {
        thisRef.obj.put(property.name, value?.arr)
    }
}

class BizArrPropertyNonNull<T: Biz>(typeParameter: KClass<T>): ArrPropertyNonNull<T>(wrapperOf(typeParameter))

class PrimArrPropertyNonNull<T: Any>() : ArrPropertyNonNull<T>(null)
*/
