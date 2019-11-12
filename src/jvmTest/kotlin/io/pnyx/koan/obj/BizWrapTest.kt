package io.pnyx.koan.obj

import org.junit.Test
import kotlin.test.assertTrue


class BizWrapTest {

    @Test
    fun doTest() {
        val p = ObjWrapper.wrap<Person>().apply {
            name = "John"
            surname = "Smith"
            addresses = Arr(
                ObjWrapper.wrap<Address>().apply {
                    street = "calle del norde"
                    nr = 12
                },
                ObjWrapper.wrap<Address>().apply {
                    street = "Trafalgar Square"
                    nr = 1
                }
            )
            favNums = Arr(13, 17)
        }
        //val n = p.name
        val addr = p.addresses?.firstOrNull()
        assertTrue {
            p.addresses!!.firstOrNull()!!.nr == 12
            &&
            p.name == "John"
            p.favNums!!.size == 2
        }
    }


    interface Person {
        var name: String?
        var surname: String?
        var addresses: Arr<Address>?
        var favNums: Arr<Int>?
    }

    interface Address {
        var street: String?
        var nr: Int?
    }

}