package com.smart.data

import java.util.ArrayList
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class GenericsExample4 {
}

internal open class B

internal class B1 : B()

fun main() {

    //contravariant - producer - out
    val arrayListB1: ArrayList<in B1> = ArrayList()
    val b = arrayListB1[0] as B
    val bOther = B()
    arrayListB1.add(bOther as B1) //would be a class cast exception at runtime

    val b1 = B1()
    arrayListB1.add(b1)

    val stringArray: Array<String> = arrayOf("a", "b")
    fill(stringArray, "c")

    val anyArray: Array<Any> = arrayOf("a", 1)
    fill(anyArray, "c")

    val charSeqArray: Array<CharSequence> = arrayOf()
    fill(charSeqArray, "1")
}

fun fill(dest: Array<in String>, value: String) {
    dest[0] = value
}

fun read(dest: Array<out String>): String {
//    dest[0] = "a"
    return dest[0]
}

//fun fillObject(dest: Array<in String>, value: Any) {
//    dest[0] = value
//}
