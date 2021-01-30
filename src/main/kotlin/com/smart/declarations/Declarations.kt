package com.smart.declarations

fun List<String>?.getLongestString(): String? {
    if (this != null) {
        return "Making ${this[0]} long here"
    }
    return null
}