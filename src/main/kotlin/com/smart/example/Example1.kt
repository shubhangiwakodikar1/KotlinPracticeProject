package com.smart.example

import com.smart.declarations.getLongestString

fun main() {
    val stringList = listOf<String>("one", "two", "three")
    val nullList = null
    println("stringList longest string = ${stringList.getLongestString()}")
    println("nullList longest string = ${nullList.getLongestString()}")
    println()

    foo(1) { println("hello") }     // Uses the default value baz = 1
    foo(qux = { println("hello") }) // Uses both default values bar = 0 and baz = 1
    foo { println("hello") }        // Uses both default values bar = 0 and baz = 1
}

fun foo(
    bar: Int = 0,
    baz: Int = 1,
    qux: () -> Unit,
) {
    println("calling foo(${bar}, ${baz}, ${qux})")
    println("qux(): ${qux()}")
    println()
}