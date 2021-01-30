package com.smart.data

sealed class SealedClass1
data class SealedData1(val data1: String): SealedClass1() {}
data class SealedData2(val data1: Int): SealedClass1() {}
data class SealedData3(val data1: Boolean): SealedClass1() {}
class Sealed4: SealedClass1() {}

fun main() {
    val sealedClass1Variable: SealedClass1 = SealedData1("sealedData1 class")
    evaluate(sealedClass1Variable)
}

fun evaluate(sealedClassVariable: SealedClass1) {
    when(sealedClassVariable) {
//        is SealedData1 -> println("sealedClassVariable is type SealedData1")
        is SealedData2 -> println("sealedClassVariable is type SealedData2")
        is SealedData3 -> println("sealedClassVariable is type SealedData3")
        is Sealed4 -> println("sealedClassVariable is type Sealed4")
    } /* no need of else logically if you cover all possibilities of the sealed class. Otherwise if you do not cover a
    particular derived class, you are taking the risk of missing out of that particular type when the when expression
    resolves to it */
}
