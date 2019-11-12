package io.pnyx.koan.obj

import kotlin.test.Test
import kotlin.test.assertTrue


interface User {
    var name: String?
    var surname: String?
    var addresses: Arr<Address>?
    var favNums: Arr<Int>?
}

interface Address {
    var street: String?
    var nr: Int?
}


class BizTest {

    @Test
    fun doTest() {
        val p = biz<User>().apply {
            name = "John"
            surname = "Smith"
            addresses = Arr(
                    biz<Address>().apply {
                        street = "calle del norde"
                        nr = 12
                    },
                    biz<Address>().apply {
                        street = "Trafalgar Square"
                        nr = 1
                    }
            )
            favNums = Arr(13, 17)
        }
        val addr = p.addresses?.firstOrNull()
        assertTrue {
            p.addresses!!.firstOrNull()!!.nr == 12
                    &&
                    p.name == "John"
            p.favNums!!.len == 2
        }
    }



}