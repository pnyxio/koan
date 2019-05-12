package io.pnyx.koan.obj

import io.pnyx.koan.io.toByteArray
import io.pnyx.koan.lang.Stack
import io.pnyx.koan.lang.utf8
import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.Output
import kotlinx.io.core.writeFully

fun jsonStringify(value: Any?, pretty: Boolean = false): String {
    val sb = StringBuilder()
    val ser = StringJsonSerializer(sb, pretty = pretty)
    JixSrc.of(value).streamTo(ser)
    return sb.toString()
}

fun jsonStringifyBytes(value: Any?, pretty: Boolean = false): ByteArray {
    val b = BytePacketBuilder()
    val ser = BinJsonSerializer(b, pretty = pretty)
    JixSrc.of(value).streamTo(ser)
    return b.toByteArray()
}

abstract class JsonSerializer(private val pretty: Boolean = false, internal val escapeSlash: Boolean = false) : JixHandler {
    private val commas = CommaInsCtx()
    private var indent: Int = 0

    abstract fun writeComma()

    abstract fun writeIndentSpaces(_indent: Int)

    abstract fun writeNewLine()

    abstract fun writeLSquare()

    abstract fun writeRSquare()

    abstract fun writeRCurly()

    abstract fun writeLCurly()

    abstract fun writeColon()

    abstract fun writeKey(key: KeyEv)

    abstract fun <T> writePrim(value: PrimOrNull<T>)

    override fun startObj() {
        commas.startObj()
        writeLCurly()
        indent++
        indent()
    }

    override fun endObj() {
        commas.endObj()
        indent--
        indent()
        writeRCurly()
    }

    override fun key(key: KeyEv) {
        commas.key(key)
        writeKey(key)
        writeColon()
    }

    override fun <T> prim(value: PrimOrNull<T>) {
        commas.prim(value)
        writePrim(value)
    }
    override fun startArr() {
        commas.startArr()
        writeLSquare()
        indent++
        indent()
    }

    override fun endArr() {
        commas.endArr()
        indent--
        indent()
        writeRSquare()
    }


    private fun indent() {
        if (pretty) {
            writeNewLine()
            writeIndentSpaces(indent)
        }
    }

    inner class CommaInsCtx : JixHandler {
        inner class Cx(val t: JsonType) {
            var firstGone = false
        }

        private val stack = Stack<Cx>()


        override fun startObj() {
            startValue()
            stack.push(Cx(JsonType.obj))
        }

        override fun endObj() {
            stack.pop()
        }

        override fun key(key: KeyEv) {
            val cx = stack.peek()!!
            if (cx.firstGone) {
                writeComma()
                indent()
            } else {
                cx.firstGone = true
            }
        }

        override fun startArr() {
            startValue()
            stack.push(Cx(JsonType.arr))
        }

        override fun endArr() {
            stack.pop()
        }

        override fun <T> prim(value: PrimOrNull<T>) {
            startValue()
        }

        private fun startValue() {
            if (stack.isEmpty()) {
                return
            }
            val cx = stack.peek()!!
            if (cx.t == JsonType.arr) {
                if (cx.firstGone) {
                    writeComma()
                    indent()
                } else {
                    cx.firstGone = true
                }
            }
        }
    }

    internal open fun indentSpaces(size: Int): String {
        if (size < INDENTS.size) {
            return INDENTS[size]
        } else {
            return indentSpaceBytes(size).utf8()
        }
    }

    internal open fun indentSpaceBytes(size: Int): ByteArray {
        if (size < INDENTS_BYTE.size) {
            return INDENTS_BYTE[size]
        } else {
            val barr = ByteArray(size)
            for (i in 0..size - 1) {
                barr[i] = ' '.toByte()
            }
            return barr
        }
    }

    companion object {
        private val INDENTS = arrayOf(
                "", "  ", "    ", "      ", "        ", "          ", "               "
        )
        private val INDENTS_BYTE: Array<ByteArray> = Array(INDENTS.size) {
            INDENTS[it].utf8()
        }
    }
}

class BinJsonSerializer(private val builder: Output, pretty: Boolean = false, escapeSlash: Boolean = false) : JsonSerializer(pretty, escapeSlash) {

    override fun <T> writePrim(value: PrimOrNull<T>) {
        JsonPrimEncoder.marshall(value.inner, builder, escapeSlash, false)
    }

    override fun writeKey(key: KeyEv) {
        JsonPrimEncoder.marshall(key.inner, builder, escapeSlash, false)
    }

    override fun writeIndentSpaces(_indent: Int) {
        builder.writeFully(indentSpaceBytes(_indent))
    }

    override fun writeComma() {
        builder.writeByte(COMMA)
    }

    override fun writeNewLine() {
        builder.writeByte(NEWLINE)
    }

    override fun writeLSquare() {
        builder.writeByte(LSQUARE)
    }

    override fun writeRSquare() {
        builder.writeByte(RSQUARE)
    }

    override fun writeRCurly() {
        builder.writeByte(RCURLY)
    }

    override fun writeLCurly() {
        builder.writeByte(LCURLY)
    }

    override fun writeColon() {
        builder.writeByte(COLON)
    }

}

class StringJsonSerializer(private val builder: Appendable, pretty: Boolean = false, escapeSlash: Boolean = false) : JsonSerializer(pretty, escapeSlash) {

    override fun <T> writePrim(value: PrimOrNull<T>) {
        JsonPrimEncoder.marshall(value.inner, builder, escapeSlash, false)
    }

    override fun writeKey(key: KeyEv) {
        JsonPrimEncoder.marshall(key.inner, builder, escapeSlash, false)
    }

    override fun writeIndentSpaces(_indent: Int) {
        builder.append(indentSpaces(_indent))
    }

    override fun writeComma() {
        builder.append(",")
    }

    override fun writeNewLine() {
        builder.append("\n")
    }

    override fun writeLSquare() {
        builder.append("[")
    }

    override fun writeRSquare() {
        builder.append("]")
    }

    override fun writeRCurly() {
        builder.append("}")
    }

    override fun writeLCurly() {
        builder.append("{")
    }

    override fun writeColon() {
        builder.append(":")
    }

}
