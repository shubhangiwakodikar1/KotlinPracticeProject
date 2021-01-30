fun interface FunctionalInterface1 {
    fun invoke(integer: Int): Boolean
}

fun main() {
    val isEvenLong = object : FunctionalInterface1 {
        override fun invoke(integer: Int): Boolean {
            return (integer % 2) == 0
        }
    }

    val isEvenLambda = FunctionalInterface1 { integer -> (integer % 2) == 0 }

    println("isEvenLong(2) = {${isEvenLong.invoke(2)}}")
    println("isEvenLambda(3) = {${isEvenLambda.invoke(3)}}")
}