package io.pnyx.koan.obj

import io.pnyx.koan.lang.Ctor
import io.pnyx.koan.lang.ctor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/*
typealias BizWrapFun<T> = (Obj) -> T
fun <T: Biz> wrapperOf(klass: KClass<T>): BizWrapFun<T> = { o: Obj -> Biz.wrap(klass, o) }

abstract class Biz constructor(var obj: Obj) {
    constructor() : this(Obj())
    init {
        require(obj.biz == null)
        obj.biz = this
    }

    companion object {
        inline operator fun <reified B: Biz> invoke(noinline builder: B.() -> Unit = {}): B   {
            val b = ctor<B>()()
            b.builder()
            return b
        }
        inline fun <reified B: Biz> wrap(obj: Obj): B {
            val b = ctor<B>()()
            b.obj = obj
            return b
        }
        fun <B: Biz> wrap(klass: KClass<B>, obj: Obj): B {
            val b : B = Ctor(klass)()
            (b as Biz).obj = obj
            return b
        }
    }
}



typealias P<T> = BizProperty<T?>
class BizProperty<T> {//aka BizProperty
operator fun getValue(thisRef: Biz, property: KProperty<*>): T? {
    val x = thisRef.obj[property.name]
    if(x is Obj) {
        var biz = x.biz
        if(biz == null) {
            @Suppress("UNCHECKED_CAST")
            //TODO miki val typeOfBiz = property.returnType.classifier as KClass<*>
            val typeOfBiz = Any::class//property.returnType.classifier as KClass<*>
            biz = Ctor(typeOfBiz)() as Biz
            biz.obj = x
        }
        @Suppress("UNCHECKED_CAST")
        return biz as T
    } else if(x == null) {
        return null
    } else {
        @Suppress("UNCHECKED_CAST")
        return x as T
    }
}

    operator fun setValue(thisRef: Biz, property: KProperty<*>, value: T?) {
        val x: Any? = when {
            value == null -> null
            value is Biz -> value.obj
            value.isPrim() -> value
            else -> throw IllegalArgumentException()
        }
        thisRef.obj.put(property.name, x)
    }
}

typealias PP<T> = BizPropertyNonNull<T>
class BizPropertyNonNull<T> {//aka BizProperty
operator fun getValue(thisRef: Biz, property: KProperty<*>): T {
    val x = thisRef.obj[property.name]
    if(x is Obj) {
        var biz = x.biz
        if(biz == null) {
            @Suppress("UNCHECKED_CAST")
            //TODO miki val typeOfBiz = property.returnType.classifier as KClass<*>
            val typeOfBiz = Any::class//property.returnType.classifier as KClass<*>
            biz = Ctor(typeOfBiz)() as Biz
            biz.obj = x
        }
        @Suppress("UNCHECKED_CAST")
        return biz as T
    } else if(x == null) {
        throw NullPointerException()
    } else {
        @Suppress("UNCHECKED_CAST")
        return x as T
    }
}

    operator fun setValue(thisRef: Biz, property: KProperty<*>, value: T) {
        val x: Any? = when(value) {
            is Biz -> value.obj
            isPrim() -> value
            else -> throw IllegalArgumentException()
        }
        thisRef.obj.put(property.name, x)
    }
}

*/
