package io.pnyx.koan.obj

import io.pnyx.koan.lang.utf8


fun parseJson(json: String): Any? {
    val b = JixBuilder()
    JsonParser(BinRange(json.utf8()), b).parse()
    return b.get()
}

fun parseJsonObj(json: String): Obj {
    val b = JixBuilder()
    JsonParser(BinRange(json.utf8()), b).parse()
    return b.getObj()
}

class JsonParser (private val range: BinRange, private val h: JixHandler) {
    private val len = range.length
    private var pos = 0
    private var cur = range.at(pos)

    fun parse() {
        expr()
    }

    private fun incr(): Byte {
        if (pos < len - 1) {
            cur = range.at(++pos)
            return cur
        } else if (pos < len) {
            ++pos
            cur = EOF
            return cur
        } else {
            throw IllegalStateException("trying to move beyond eof")
        }
    }


    private fun stayOrIncrToNextNonWs(): Byte {
        while (true) {
            when (cur) {
                SPACE, NEWLINE, TAB, CARRIAGE_RET -> {
                }
                else -> return cur
            }
            incr()
        }
    }

    private fun eof() {
        while (pos < len - 1) {
            when (cur) {
                SPACE, NEWLINE, TAB, CARRIAGE_RET -> {}
                else -> throw IllegalArgumentException("trailing garbage at position $pos")
            }
            incr()
        }
    }


    private fun expr() {
        stayOrIncrToNextNonWs()
        any()
        eof()
    }

    private fun any() {
        stayOrIncrToNextNonWs()
        when (cur) {
            LCURLY -> {
                obj()
            }
            LSQUARE -> {
                array()
            }
            DASH, _0, _1, _2, _3, _4, _5, _6, _7, _8, _9 -> {
                num()
            }
            QUOT -> {
                str()
            }
            _t -> {
                _true()
            }
            _f -> {
                _false()
            }
            _n -> {
                _null()
            }
            else -> throw IllegalArgumentException("unespected char " + cur.toChar() + " at position " + pos)
        }
    }

    private fun _true() {
        if (incr() != _r || incr() != _u || incr() != _e) {
            throw IllegalArgumentException("invalid literal at position $pos")
        }
        incr()
        h.prim(True)
    }

    private fun _false() {
        if (incr() != _a || incr() != _l || incr() != _s || incr() != _e) {
            throw IllegalArgumentException("invalid literal at position $pos")
        }
        incr()
        h.prim(False)
    }

    private fun _null() {
        if (incr() != _u || incr() != _l || incr() != _l) {
            throw IllegalArgumentException("invalid literal at position $pos")
        }
        incr()
        h.prim(Null)
    }

    private fun str() {
        h.prim(StringEv(readstr()))

    }

    private fun readstr(): String {
        val start = pos
        var backSlashSeen = false
        while (true) {
            incr()
            if (backSlashSeen) {
                when (cur) {
                    BACKSLASH, QUOT, SLASH, _b, _f, _n, _r, _t -> backSlashSeen = false
                    _u -> {
                        for (j in 0..3) {
                            if (!(incr() > SLASH && cur < COLON || cur >= _a && cur <= _f)) {
                                throw IllegalStateException("illegal unicode digit $cur at position $pos")
                            }
                        }
                        backSlashSeen = false
                    }
                    else -> throw IllegalArgumentException("illegal escape code $cur at position $pos")
                }
            } else {
                when (cur) {
                    BACKSLASH -> backSlashSeen = true
                    QUOT -> {
                        incr()
                        return range.range(start + 1, pos - start - 2).utf8()
                    }
                    else -> {
                    }
                }

            }
        }
    }

    private fun num() {
        val start = pos
        var dotSeen = false
        while (true) {
            incr()
            when (cur) {
                DOT -> if (dotSeen) {
                    throw IllegalArgumentException("malformed number at position$pos")
                } else {
                    dotSeen = true
                }
                DASH -> if (pos != start) {
                    throw IllegalArgumentException("usespected not leading '-' in number at position$pos")
                }
                _0, _1, _2, _3, _4, _5, _6, _7, _8, _9 -> {
                }
                EOF -> {
                    h.prim(NumberEv(asNum(range.range(start, pos - start))))
                    return
                }//TODO ????
                else -> {
                    h.prim(NumberEv(asNum(range.range(start, pos - start))))
                    return
                }
            }
        }
    }

    private fun asNum(range: ByteArray): Number {
        val s = range.utf8()
        if (s.indexOf('.') >= 0 || s.indexOf('e') >= 0 || s.indexOf('E') >= 0) {
            return s.toDouble()
        } else {
            val l = s.toLong()
            return if (l > Int.MAX_VALUE || l < Int.MIN_VALUE) {
                l
            } else {
                l.toInt()
            }
        }
    }

    private fun array() {
        var first = true
        h.startArr()
        incr()
        while (true) {
            stayOrIncrToNextNonWs()
            if (cur == RSQUARE) {
                h.endArr()
                incr()
                return
            }
            if (!first) {
                comma()
            }
            stayOrIncrToNextNonWs()
            any()
            first = false
        }
    }

    private fun obj() {
        h.startObj()
        var first = true
        incr()
        while (true) {
            stayOrIncrToNextNonWs()
            if (cur == RCURLY) {
                incr()
                h.endObj()
                return
            }
            if (!first) {
                comma()
            }
            stayOrIncrToNextNonWs()
            keyAndColon()
            stayOrIncrToNextNonWs()
            any()
            first = false
        }
    }

    private fun comma() {
        if (cur != COMMA) {
            throw IllegalArgumentException("not found expected comma at $pos")
        }
        incr()
    }

    private fun keyAndColon() {
        val k = readstr()
        stayOrIncrToNextNonWs()
        if (cur != COLON) {
            throw IllegalArgumentException("not found expected colon at $pos")
        }
        incr()
        h.key(KeyEv(k))
    }

    companion object {
        private val EOF: Byte = 0
    }
}


class BinRange(private val bb: ByteArray) {

    val length: Int
        get() = bb.size

    internal fun at(pos: Int): Byte {
        return bb[pos]
    }

    fun range(start: Int, len: Int): ByteArray {
        return bb.copyOfRange(start, start+len)
    }

}

