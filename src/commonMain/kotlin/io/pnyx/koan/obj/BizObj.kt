package io.pnyx.koan.obj

expect inline fun <reified T> biz(): T
expect inline fun <reified T> biz(o: Obj): T
