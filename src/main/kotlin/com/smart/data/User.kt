package com.smart.data

data class User(val name: String, val age: Int) {
}

fun main() {
    val user1: User = User("John", 40)
    val user2: User = user1.copy(age = 5)

    println("user1: ${user1}")
    println("user2: ${user2}")

    //component functions
    println("user2.component1(): ${user2.component1()}")
    println("user2.component2(): ${user2.component2()}")

    //destructuring declarations
    val jane = User("Jane", 35)
    val (name, age) = jane
    println("$name, $age years of age") // prints "Jane, 35 years of age"
}