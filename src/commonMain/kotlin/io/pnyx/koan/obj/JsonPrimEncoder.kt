package io.pnyx.koan.obj

import io.pnyx.koan.io.toByteArray
import io.pnyx.koan.io.writeStringUtf8
import io.pnyx.koan.lang.toStringPretty
import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.Output
import kotlinx.io.core.writeFully


//TODO test , test unicode escapeAll
object JsonPrimEncoder {

    //also quotes strings !!
    fun toJsonString(v: Any?): String {
        val sw = StringBuilder()
        toJsonString(v, sw)
        return sw.toString()
    }

    //also quotes strings !!
    fun toJsonString(v: Any?, out: Appendable) {
        marshall(v, out)
    }


    //also quotes strings !!
    fun toJsonBytes(v: Any?): ByteArray {
        val os = BytePacketBuilder()
        toJsonBytes(v, os)
        return os.toByteArray()
    }

    //also quotes strings !!
    fun toJsonBytes(v: Any?, out: Output) {
        marshall(v, out)
    }

    //also quotes strings !!
    internal fun marshall(value: Any?, w: Appendable, escSlash: Boolean = false, escUnicode: Boolean = false) =
        when (value) {
            null -> w.append("null")
            is String -> {
                w.append("\"")
                marhallNoQuoteTo(value, w, escSlash, escUnicode)
                w.append("\"")
            }
            is Number -> w.append(value.toStringPretty())
            is Boolean -> w.append(value.toString())
            else -> throw IllegalArgumentException()
        }

    //also quotes strings !!
    internal fun marshall(value: Any?, out: Output, escSlash: Boolean = false, escUnicode: Boolean = false) =
        when (value) {
            null -> out.writeFully(NULL_ON_WIRE)
            is String -> {
                out.writeByte(QUOT)
                marhallNoQuoteTo(value, out, escSlash, escUnicode)
                out.writeByte(QUOT)
            }
            is Number -> marhallNoQuoteTo(value.toStringPretty(), out, false, false)
            is Boolean -> out.writeFully(if (value) TRUE_ON_WIRE else FALSE_ON_WIRE)
            else -> throw IllegalArgumentException()
        }

    /**
     * \" \\ \/ \b \f \n \r \t \u1234 four-hex-digits
     */
    private fun marhallNoQuoteTo(s: String, writer: Appendable, escSlash: Boolean, escUnicode: Boolean) {
        for (c in s) {
            when (c) {
                '"' -> writer.append("\\\"")
                '/' -> writer.append(if (escSlash) "\\/" else "/")
                '\\' -> writer.append("\\\\")
                '\b' -> writer.append("\\b")
//TODO                    '\f' -> writer.write("\\f")
                '\n' -> writer.append("\\n")
                '\r' -> writer.append("\\r")
                '\t' -> writer.append("\\t")
                else -> if (escUnicode) {
                    if (c.toInt() < 127) {
                        writer.append(c)
                    } else {
                        val hex = c.toInt().toString(16)
                        writer.append("\\u")
                        when (hex.length) {
                            1 -> writer.append("000")
                            2 -> writer.append("00")
                            3 -> writer.append("0")
                            else -> {
                            }
                        }
                        writer.append(c.toInt().toString(16))
                    }
                } else {
                    writer.append(c)
                }
            }
        }
    }

    private fun marhallNoQuoteTo(s: String, os: Output, escSlash: Boolean, escUnicode: Boolean) {
        for (c in s) {
            when (c) {
                '/' -> {
                    if (escSlash) {
                        os.writeByte(BACKSLASH)
                    }
                    os.writeByte(SLASH)
                }
                '"' -> {
                    os.writeByte(BACKSLASH)
                    os.writeByte(QUOT)
                }
                '\\' -> {
                    os.writeByte(BACKSLASH)
                    os.writeByte(BACKSLASH)
                }
                '\b' -> {
                    os.writeByte(BACKSLASH)
                    os.writeByte(_b)
                }
                //TODO '\f' -> {
//                        os.writeByte(BACKSLASH)
//                        os.writeByte(_f)
//                    }
                '\n' -> {
                    os.writeByte(BACKSLASH)
                    os.writeByte(_n)
                }
                '\r' -> {
                    os.writeByte(BACKSLASH)
                    os.writeByte(_r)
                }
                '\t' -> {
                    os.writeByte(BACKSLASH)
                    os.writeByte(_t)
                }
                else -> if (escUnicode) {
                    if (c.toInt() < 127) {
                        os.writeByte(c.toByte())
                    } else {
                        val hex = c.toInt().toString(16)
                        os.writeByte(BACKSLASH)
                        os.writeByte(_u)
                        when (hex.length) {
                            1 -> {
                                os.writeByte(_0)
                                os.writeByte(_0)
                                os.writeByte(_0)
                            }
                            2 -> {
                                os.writeByte(_0)
                                os.writeByte(_0)
                            }
                            3 -> os.writeByte(_0)
                            else -> {
                            }
                        }
                        val hx = c.toInt().toString(16)
                        os.writeByte(hx[0].toByte())
                        os.writeByte(hx[1].toByte())
                    }
                } else {
                    os.writeStringUtf8(c.toString())
                }
            }
        }
    }
}
