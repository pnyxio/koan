package io.pnyx.koan.lang

import kotlin.reflect.KClass
import kotlin.reflect.KParameter


/*
object Refl {

    inline fun <reified T:Any> ctor(vararg args: Any?) : T {
        return klassCtor(T::class, *args)
    }
    fun <T:Any> klassCtor(klass: KClass<T>, vararg args: Any?) : T {
        try {
            val firstCompatCtor  = klass.constructors
                .filter {it.parameters.size == args.size}
                .filter { it.parameters.filterIndexed  {i, p -> argCompat(args[i], p) }.size == it.parameters.size }
                .first()
            val t: T = firstCompatCtor.call(*args)
            return t
        } catch (e: Exception) {
            throw RuntimeException("cannot find constructor of $klass for arguments ${args.joinToString(", ", "(", ")")}")
        }
    }

    fun argCompat(arg: Any?, par: KParameter) = when {
            arg == null && par.type.isMarkedNullable -> true
            arg == null -> false
            (par.type.classifier as KClass<*>).isInstance(arg) -> true
            else -> false
        }

    fun <T: Any> emptyCtor(klass: KClass<T>, builder: (T.()->Unit) = {}): T {
        try {
            val firstCompatCtor  = klass.constructors
                    .filter {it.parameters.size == 0}
                    .first()
            val t: T = firstCompatCtor.call()
            t.builder()
            return t
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
*/
//limitation: in js calls the base constructor
expect class Ctor<T:Any> constructor(klass: KClass<T>) {
    operator fun invoke(vararg args: Any?): T

}

inline fun <reified T:Any> ctor() = Ctor(T::class)
