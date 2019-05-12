package io.pnyx.koan.lang

import kotlin.test.*


class Addr(var street: String, var num: Number?) {
}

class ReflTest {


    @Test
    fun testReflCtor() {
        assertEquals("puzzu", ctor<Addr>()("puzzu", 12).street)
        //TODO assertNull(ctor<Addr>()().num)
        assertNull(ctor<Addr>()("foo", null).num)
        assertEquals(12.1, ctor<Addr>()("foo", 12.1).num)
        assertEquals("foo", ctor<Addr>()("foo", null).street)
//TODO        assertTrue { ctor<Addr>()() is Addr }
    }

//TODO    @Test
    fun testReflCtorBuilderPattern() {
        val addr = ctor<Addr>()().apply {
            street = "miau"
            num = 11
        }
        assertEquals(11, addr.num)
        assertEquals("miau", addr.street)
    }

}