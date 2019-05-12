package io.pnyx.koan.obj

import io.pnyx.koan.lang.Stack
import io.pnyx.koan.lang.illegalState


class JixBuilder: JixHandler {
    private var result: Any? = null
    private var cur: Any? = null
    private var pendingKey: KeyEv? = null
    private val parentHierarchy = Stack<Any?>()
    private var consumed = false

    fun get(): Any? {
        require(parentHierarchy.isEmpty()) { "trailing events" }
        consumed = true
        return result

    }

    @Suppress("UNCHECKED_CAST")
    fun getArr(): Arr = get() as Arr

    fun getObj(): Obj = get() as Obj

    private fun unconsumed() = if(consumed) illegalState() else Unit

    private var subscribed = false;


    override fun <T> prim(value: PrimOrNull<T>) {
        unconsumed()
        if(result == null) {
			result = value.inner
			cur = result
		} else if(cur is TArr<*>) {
            @Suppress("UNCHECKED_CAST")
            (cur as TArr<T>).push(value.inner)
		} else {
			(cur as Obj)[pendingKey!!.inner] = value.inner
		}

    }

    override fun key(key: KeyEv) {
        unconsumed()
        pendingKey = key
    }

    override fun startObj() {
        unconsumed()
        if(result == null) {
			result = Obj()
			cur = result;
		} else {
			if(cur is TArr<*>) {
                @Suppress("UNCHECKED_CAST")
                cur = (cur as TArr<Obj>).push(Obj())
			} else {
				cur = (cur as Obj).store(pendingKey!!.inner, Obj());
			}
		}
		parentHierarchy.push(cur);

    }

    override fun endObj() {
        unconsumed()
        parentHierarchy.pop();
		cur = if(parentHierarchy.isEmpty()) null else parentHierarchy.peek();

    }

    override fun startArr() {
        unconsumed()
        if(result == null) {
			result = Arr();
			cur = result
		} else {
			if(cur is TArr<*>) {
                @Suppress("UNCHECKED_CAST")
				cur = (cur as Arr).push(Arr())
			} else {
				cur = (cur as Obj).store(pendingKey!!.inner, TArr())
			}
		}
		parentHierarchy.push(cur);
    }

    override fun endArr() {
        unconsumed()
        parentHierarchy.pop();
		cur = if(parentHierarchy.isEmpty()) null else parentHierarchy.peek();
    }

}

