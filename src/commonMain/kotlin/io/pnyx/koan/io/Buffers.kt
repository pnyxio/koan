package io.pnyx.koan.io

import kotlinx.io.charsets.Charsets
import kotlinx.io.core.*

fun BytePacketBuilder.toByteArray(): ByteArray {
    val brp = build()
    val barr = ByteArray(brp.remaining.toInt())
    brp.readAvailable(barr)
    brp.release()
    release()
    return barr
}

fun Output.writeStringUtf8(s: String) {
    append(s, 0, s.length)
}

