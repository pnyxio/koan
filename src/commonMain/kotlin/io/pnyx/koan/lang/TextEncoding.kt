package io.pnyx.koan.lang

import kotlinx.io.charsets.Charsets
import kotlinx.io.charsets.Charsets.UTF_8
import kotlinx.io.charsets.encodeToByteArray
import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.String
import kotlinx.io.core.readText
import kotlinx.io.core.toByteArray

val utf8Enc = UTF_8.newEncoder()
val utf8Dec = UTF_8.newDecoder()

fun String.utf8(): ByteArray = utf8Enc.encodeToByteArray(this)

fun ByteArray.utf8(): String = String(this, 0, size, UTF_8)//(utf8Dec.decode(IoBuffer(this))


private fun ByteArray.hexdump() = joinToString(separator = " ") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }


//fun ByteArray.utf8(): String {
//    val sb = StringBuilder()
//    ByteReadPacket(this).readText(out = sb, charset = Charsets.UTF_8)
//    return sb.toString()
//}
//
//fun String.utf8(): ByteArray {
//    return this.toByteArray(Charsets.UTF_8)
//}

fun ByteArray.ascii(): String {
    val sb = StringBuilder()
    ByteReadPacket(this).readText()
    return sb.toString()
}

fun String.ascii(): ByteArray {
    return ByteArray(length) {
        this[it].toByte()
    }
}