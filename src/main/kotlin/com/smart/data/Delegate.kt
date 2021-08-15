package com.smart.data

import kotlin.reflect.KProperty

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}

class ExampleOfDelegate() {
    var property1 : String by Delegate()
}

fun main() {
    val exampleOfDelegate1 = ExampleOfDelegate()
    println("beforeChange: exampleOfDelegate1.property1= ${exampleOfDelegate1.property1}")

    exampleOfDelegate1.property1 = "newValue"
    println("afterChange: exampleOfDelegate1.property1= ${exampleOfDelegate1.property1}")
}