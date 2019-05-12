package io.pnyx.koan.obj

import io.pnyx.koan.lang.utf8
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class JsonSerializerTest {

    @Test
    fun testUtf8() {
        val test = Obj(
            "title" to "Pourquoi chérie",
            "a" to Arr(
                "miau", null, false, 1.1, 2.0
            )
        )
        val payloadBinary = jsonStringifyBytes(test)
        val payloadString = jsonStringify(test).utf8()

        assertTrue {
            payloadBinary contentEquals payloadString
        }
        assertEquals("{\"title\":\"Pourquoi chérie\",\"a\":[\"miau\",null,false,1.1,2]}", jsonStringify(test, false))
    }

}
