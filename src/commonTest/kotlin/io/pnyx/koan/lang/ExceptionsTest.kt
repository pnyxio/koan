package io.pnyx.koan.lang

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ExceptionsTest {

    @Suppress("UNREACHABLE_CODE")
    @Test
    fun raiseTest() {
        try {
            val anEx = RuntimeException("over the rainbow")
            raise<SecurityException>("somewhere", anEx)
            fail()
        } catch(e: SecurityException) {
            assertEquals("somewhere over the rainbow", "${e.message} ${e.cause?.message}")
        }
    }

    @Suppress("UNREACHABLE_CODE")
    @Test
    fun assertionsTest() {
        try {
            illegalState("cucu")
            fail()
        } catch(e: IllegalStateException) {
            assertEquals("cucu", "${e.message}")
        }
        try {
            val x = 1
            illegalStateAssert("x = $x") { x == 2 }
            fail()
        } catch(e: IllegalStateException) {
            assertEquals("x = 1", "${e.message}")
        }
    }

}