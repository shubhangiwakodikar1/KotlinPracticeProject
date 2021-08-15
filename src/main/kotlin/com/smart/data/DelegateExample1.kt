package com.smart.data

class DelegateExample1 {}

interface Base {
    fun printMessage()
    fun printMessageLine()
}

class BaseImpl(private val x: Int) : Base {
    override fun printMessage() { print(x) }
    override fun printMessageLine() {
        println("Shubhangi")
        println(x)
    }
}

class Derived(b: Base) : Base by b {
    override fun printMessage() { print("abc") }
}

fun main() {
    val b = BaseImpl(10)
    Derived(b).printMessage()
    Derived(b).printMessageLine()
}