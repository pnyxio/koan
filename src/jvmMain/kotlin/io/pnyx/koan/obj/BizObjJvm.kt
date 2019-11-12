package io.pnyx.koan.obj

import io.pnyx.koan.lang.illegalArgAssert
import io.pnyx.koan.lang.illegalState
import io.pnyx.koan.lang.isInterface
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaGetter
import kotlin.reflect.jvm.javaSetter

actual inline fun <reified T> biz(): T = ObjWrapper.wrap<T>()
actual inline fun <reified T> biz(o: Obj): T = ObjWrapper.wrap<T>(o)


interface ObjWrapper {

    companion object {
        inline fun <reified T> wrap(o: Obj? = null) : T {
            val obj = o ?: Obj()
            val kClass = T::class
            illegalArgAssert("$kClass is not an interface") { kClass.isInterface }
            val props = businessPropsOf(kClass)


            val h = ObjWrapperInvocationHandler(obj, T::class.java, props)
            return Proxy.newProxyInstance(obj.javaClass.classLoader, arrayOf(T::class.java), h) as T
        }

        fun businessPropsOf(kClass: KClass<*>): Map<Method, MethodHandler> {
            val props = kClass.members.filter {
                it is KProperty
            }.flatMap {
                PropertyIntrospector(it as KProperty).handlers
            }.associateBy { it.method }
            return props
        }
    }
}

sealed class MethodHandler(val method: Method, val propName: String)

class PrimitiveGet(method: Method, propName: String) : MethodHandler(method, propName)

class PrimitiveSet(method: Method, propName: String) : MethodHandler(method, propName)

class BeanGet(method: Method, propName: String, val beanType: KClass<*>) : MethodHandler(method, propName)

class BeanSet(method: Method, propName: String, val beanType: KClass<*>) : MethodHandler(method, propName)

class ArrGet(method: Method, propName: String, val beanType: KClass<*>) : MethodHandler(method, propName)

class ArrSet(method: Method, propName: String, val beanType: KClass<*>) : MethodHandler(method, propName)


class PropertyIntrospector constructor(val prop: KProperty<*>) {
    val handlers : List<MethodHandler> get() {
        val writable = prop is KMutableProperty
        val mutProp = if(writable) prop as KMutableProperty else null
        val javaSetter = if(writable) mutProp!!.javaSetter else null
        val javaGetter = prop.javaGetter!!
        val returnType = prop.returnType
        //val javaType = returnType.javaType
        val nullable = returnType.isMarkedNullable
        val klass = returnType.classifier as KClass<*>
        if(klass.isJsonPrimitive()) {
            if(writable) {
                return listOf(PrimitiveGet(javaGetter, prop.name), PrimitiveSet(javaSetter!!, prop.name))
            } else {
                return listOf(PrimitiveGet(javaGetter, prop.name))
            }
        } else if(klass == Arr::class) {
            val proj = returnType.arguments.first()
            val projType = proj.type!!.classifier as KClass<*>
            if(writable) {
                return listOf(ArrGet(javaGetter, prop.name, projType), ArrSet(javaSetter!!, prop.name, projType))
            } else {
                return listOf(ArrGet(javaGetter, prop.name, projType))
            }
        } else {//biz
            //TODO check is really biz else return empty list
            if(writable) {
                return listOf(BeanGet(javaGetter, prop.name, klass), BeanSet(javaSetter!!, prop.name, klass))
            } else {
                return listOf(BeanGet(javaGetter, prop.name, klass))
            }
        }
    }
}


internal fun doProxy(o: Obj, role: KClass<*>) : Proxy {
    val h = ObjWrapperInvocationHandler(o, role.java, ObjWrapper.businessPropsOf(role))
    val prox = Proxy.newProxyInstance(role.java.classLoader, arrayOf(role.java), h) as Proxy
    return prox
}

class ObjWrapperInvocationHandler(private val obj: Obj, private val role: Class<*>, private val propHandlers: Map<Method, MethodHandler>) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? {
        val methodHandler = propHandlers[method]
        if(methodHandler == null) {//TODO or is unwrap if still needed
            return method.invoke(proxy, args)
        } else {
            val pName = methodHandler.propName
            return when(methodHandler) {
                is PrimitiveGet -> obj.fetch(pName)
                is PrimitiveSet -> {
                    obj.store(pName, fromBizOm(args!![0]))
                    null
                }
                is BeanGet -> {
                    val value = obj.fetch(pName)
                    if(value == null) {
                        null
                    } else if(value is Obj) {
                        if(value is ObjImpl) {
                            value.proxy ?: run {
                                val prox = doProxy(value, methodHandler.beanType)
                                value.proxy = prox
                                prox
                            }
                        } else {
                            illegalState("unsupported object model found")
                        }
                    } else {//PANIC
                        illegalState("TODO PANIC")
                    }
                }
                is BeanSet -> {
                    val value = args!![0]
                    if(value == null) {
                        obj.store(pName, null)
                        null
                    } else {
                        if(value is Proxy) {
                            val _o: Obj = fromBizOm(value) as Obj
                            obj.store(pName, _o)
                            null
                        } else {
                            illegalState("TODO PANIC")
                        }
                    }
                }
                is ArrGet -> {
                    val componentType = methodHandler.beanType
                    val arr = obj.fetch(pName) as Arr<*>?
                    if(arr == null) {
                        arr
                    } else {
                        val bizArr = ArrImpl<Any?>()
                        for(item in arr) {
                            bizArr.append(toBizOm(item, componentType))
                        }
                        bizArr
                    }
                }
                is ArrSet -> {
                    var bizArr = args!![0] as Arr<*>?
                    if(bizArr == null) {
                        obj.store(pName, null)
                    } else {
                        val jsonArr = Arr.ofAny()
                        for(item in bizArr) {
                            jsonArr.append(fromBizOm(item))
                        }
                        obj.store(pName, jsonArr)
                    }
                    null
                }
            }
        }
    }

    private fun fromBizOm(value: Any?): Any? {
        if (value.isPrimOrNull()) {
            return value
        } else if (value is Proxy) {
            val hField = Proxy::class.java.getDeclaredField("h")
            //TODO avoid this, how ?
            hField.setAccessible(true);
            val ih = hField.get(value) as ObjWrapperInvocationHandler
            return ih.obj
        } else if (value is Arr<*>) {
            val jsonArr = Arr.ofAny()
            for(item in value) {
                jsonArr.append(fromBizOm(item))
            }
            return jsonArr
        } else {
            illegalState("unexpected business type ${value!!::class}")
        }
    }
    private fun toBizOm(value: Any?, bizType: KClass<*>?): Any? {
        if (value.isPrimOrNull()) {
            return value
        } else if (value is Obj) {
            if (value is ObjImpl) {
                return value.proxy ?: run {
                    val prox = doProxy(value, bizType!!)
                    value.proxy = prox
                    prox
                }
            } else {
                illegalState("unsupported object model found")
            }
        }//TODO nested Arr
        return null
    }
}
