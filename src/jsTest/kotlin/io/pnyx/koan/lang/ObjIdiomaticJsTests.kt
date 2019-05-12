package io.pnyx.koan.lang

import io.pnyx.koan.obj.JssArr
import io.pnyx.koan.obj.get
import kotlinext.js.jsObject
import kotlin.test.Test
import kotlin.test.assertTrue

class ObjIdiomaticJsTests {
    //val o = Object.create(1, jsObject {} /* = compiled code */)

    @Test
    fun test1() {
        val pino = callTheNet()

        verifyContact(pino)


    }

//    @Test
    fun test2() {
        val contacts = jsArr<Contact>()
        contacts.push(jsObject<Contact> {
            name = "gino"
        })
        val c = jsObject<Contact> {
            name = "pino"
            friends = //contacts
                jsArr2 {
                    add(jsObject<Contact> {
                        name = "gino"
                    })
                }
        }
        verifyContact(c)
    }

//    @Test
    fun test3() {
//
//        assertEquals("pino", pino.name)
//
//
//        val contact = jsObject<Contact> {
//            name = "gino"
//            friends = null
//        }
    }


}

fun callTheNet(): Contact {
    val c = js("{ \"name\" : \"pino\" , \"friends\" : [{ \"name\" : \"gino\" }]}")

    return c
}

fun postToNet(c: Contact) = verifyContact(c)

fun verifyContact(c: Contact) {
    print(":::")
    println(c.friends)
    assertTrue {
        c.name == "pino"
//        c.friends!!.first().name == "gino"
        c.friends!!.get(0).name == "gino"
    }
}

interface Contact {
    var name: String?
    var friends: JssArr<Contact>?
}

