package com.smart.example

data class Example2<out S>(val str: S) {
    fun todofunction() {
        return TODO()
    }

    fun anotherFunction(parameterName: String?) {
        val first = parameterName?.first() ?: throw NoSuchFieldError()
    }

    fun returnTypeS(): S {
        return str
    }

//    fun acceptTypeS(str: S) {
//        print("acceptTypeS")
//    }
}

//data class Example4<in S>(val str: S) {
//    fun todofunction() {
//        return TODO()
//    }
//
//    fun anotherFunction(parameterName: String?) {
//        val first = parameterName?.first() ?: throw NoSuchFieldError()
//    }
//
////    fun returnTypeS(): S {
////        return str
////    }
//
//    fun acceptTypeS(str: S) {
//        print("acceptTypeS")
//    }
//}

fun main() {
    val example2: Example2<*> = Example2(1)
    print("example2: ${example2}")
    print("example2.returnTypeS(): ${example2.returnTypeS()}")
//    print("example2.acceptTypeS(1): ${example2.acceptTypeS(1)}")


    print("nothing: ${example2.todofunction()}")
}