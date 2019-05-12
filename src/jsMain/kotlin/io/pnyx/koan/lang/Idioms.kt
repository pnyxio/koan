package io.pnyx.koan.lang

import kotlinext.js.Object

fun <T> jsNew(jsCtor: dynamic, vararg args: Any?): T =
    js("args.unshift(null); new (Function.prototype.bind.apply(jsCtor, args));")


fun <T> T.copy(vararg src : Any) : T {
    val o = kotlinext.js.js {}
    kotlinext.js.assign(o, this)
    for (s in src) {
        kotlinext.js.assign(o, s)
    }
    return o
}

fun <T> T.copy(vararg src : Any, builder: T.() -> Unit) : T {
    val o = this.copy(*src)
    o.builder()
    return o
}

fun Any.copyDyn(vararg src : Any, builder: dynamic.() -> Unit) : dynamic {
    val o: dynamic = this.copy(*src)
    o.builder()
    return o
}


val ObjectConstructor = Object.getPrototypeOf<dynamic, Any>(Object).constructor

fun <T> jso(o : T): T = o.also {
    Object.getPrototypeOf<dynamic, T>(o).constructor = ObjectConstructor
}

fun <T> T.jso(): T = this.also {
    Object.getPrototypeOf<dynamic, T>(this).constructor = ObjectConstructor
}


//fun beget(o: Any) : dynamic {
//    return Object.create(o).asDynamic()
//}
//
//fun beget(o: Any, builder: dynamic.() -> Unit) : dynamic {
//    val x = Object.create(o).asDynamic()
//    builder.invoke(x)
//    return x
//}

//fun Any.jsCopyPropsTo(target: Any) : Unit {
//    val it = target.asDynamic()
//    val me = this.asDynamic()
//    for( p in Object.keys(this)) {
//        it[p] = me[p]
//    }
//}
