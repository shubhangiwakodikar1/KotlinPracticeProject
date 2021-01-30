package com.smart.data

data class Person(val name: String) {
    var age: Int = 0
}

fun main() {
    val person1: Person = Person("John")
    val person2: Person = Person("John")
    person1.age = 10
    person2.age = 20
    println("person1 == person2: ${person1 == person2}")
    println("person1: ${person1}")
    println("person2: ${person2}")

    //copy
    val person3 = person1.copy(name="John3")
    println("person3: ${person3}")
}