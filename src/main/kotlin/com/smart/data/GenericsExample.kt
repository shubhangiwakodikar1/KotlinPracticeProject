package com.smart.data

class Box<T>(t: T) {
    var value = t

    companion object {
        infix fun from(triangle: Triangle): Box<Triangle> {
            return Box<Triangle>(triangle)
        }
    }

    infix fun combine(triangle: Triangle): Box<Any> {
        val boxCombined = Box<Any>(triangle)
        return boxCombined
    }
}

val predicate1: () -> Boolean = {true}
//fun (() -> Boolean).negate(): () -> Boolean {
//    return {!this()}
//}

val (() -> Boolean).negate1: () -> Boolean get() = {
    !this()
}

//function type or higher order type. Anonymous function.
val predicateWithStrArg: (stringArg: String) -> Boolean = {stringArg -> stringArg.equals("abc") }
val ((stringArg: String) -> Boolean).negate: (stringArg: String) -> Boolean get() = {
    !this(it)
}

fun Box<Any>.extendFunc() {
    println("extention function of Box")
}

class Triangle {

}

fun main() {
    val box = Box(1)
    val box1: Box<Int> = Box(2)
    println("box.value = ${box.value}")
    println("box1.value = ${box1.value}")

    println("predicate.negate() = ${predicate1.negate1()}")
    //MVI Model View Interaction
    println("predicateWithStrArg(abc) = ${predicateWithStrArg("abc")}")
    println("predicateWithStrArg.negate(abc) = ${predicateWithStrArg.negate("abc")}")
    println("1.times(2) is ${1.times(2)}")

    val n = 2
    val booleanN = true
    val value1 = n * (if(booleanN) 1 else 2)
    println("boolean arithmetic operation: $value1")

    println("Box from Triangle(): ${Box from Triangle()}")
    val triangle: Triangle = Triangle()
    val box2: Box<Triangle> = Box<Triangle>(triangle)
    println("Box.combine(): ${box2 combine triangle}")
}