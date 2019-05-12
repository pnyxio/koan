package io.pnyx.koan.lang

import kotlinext.js.Object
import kotlin.test.Test

class JsLangTests {

    class Human(val name: String)

    @Test
    fun testPojso() {
        val h = Human("john")

        val proto = Object.getPrototypeOf<dynamic, Any>(h)
        //proto.constructor = ObjectConstructor
        //val c =js("h.prototype.constructor")
        println(js("proto.constructor.toString()"))
        println(h.jso().name)
        println(js("proto.constructor.toString()"))
    }
}