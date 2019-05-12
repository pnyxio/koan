package io.pnyx.koan.io

class ParamMap() {//: MutableMap<<String, List<String>>> {
    private var m = LinkedHashMap<String,Array<String>>()

    fun append(name: String , value: String) {
        var arr = m[name]
        if(arr != null) {
            m[name] = arr + value
        } else {
            m[name] = arrayOf(name)
        }
    }
    fun delete(name: String)  {
        m.remove(name)
    }
    fun get(name: String): String? = m[name]?.get(0)

    fun getAll(name: String): List<String> = m[name]?.asList() ?: ArrayList()

    fun has(name: String): Boolean = m.contains(name)

    fun set(name: String, value: String) {
        m[name] = arrayOf(value)
    }

    fun sort() {
        val old = m
        m = LinkedHashMap(m.size)
        old.entries .sortedBy { it.key }.forEach { m.entries.add(it) }
    }


    //stringifier
    override fun toString(): String = ""

    fun toMapOfArrays() {

    }
    fun toMapOfFirsts() {

    }


    companion object {
        //[Constructor(optional (sequence<sequence<String>> or record<String, String> or String) init = ""),
    }
}
