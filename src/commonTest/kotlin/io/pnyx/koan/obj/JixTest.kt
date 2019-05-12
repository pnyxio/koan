package io.pnyx.koan.obj

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JixTest {

    @Test
    fun simpleBuildTest() {
        var b = JixBuilder()
        b.prim(Null)
        assertTrue {
            b.get() == null
        }
        try {
            b.getArr()
        } catch (e: Exception) {
        }

        b = JixBuilder()
        b.prim(StringEv("hello"))
        assertEquals("hello", b.get())

        b = JixBuilder()
        b.startArr()
        b.prim(StringEv("hello"))
        b.endArr()
        assertEquals(1, b.getArr().size)

        b = JixBuilder()
        b.startObj()
        //b.onStartEntry()
        b.key(KeyEv("message"))
        b.prim(StringEv("hello"))
        //b.onEndEntry()
        b.endObj()
        assertEquals(1, b.getObj().size)
        assertEquals("hello", b.getObj()["message"])

    }

    @Test
    fun parseTest() {
        assertNull(parseJson("null"))
        val o = parseJsonObj("""
            |{
            |   "b": true,
            |   "n": 123.3,
            |   "s" : "",
            |   "z"    :    null,
            |   "a" : [1,{"x":"y"}
            |   ],
            |   "o": {
            |       "o":{"x":"y"}
            |   }
            |}
        """.trimMargin())
        assertNull(o["z"])
        assertTrue(o.containsKey("z"))
        assertTrue(o["b"] as Boolean)
        assertEquals(123.3, o["n"])
        assertEquals("", o["s"])
        val a = o["a"].asArr
        assertEquals(1, a[0])
        assertEquals("y", (a[1] as Obj)["x"])
   }
}