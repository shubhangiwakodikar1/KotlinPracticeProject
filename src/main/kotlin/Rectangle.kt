import kotlin.properties.Delegates

open class Rectangle {
    open fun draw() {
        println("Drawing a rectangle")
    }
    val borderColor: String
        get() {
            println("getting borderColor")
            return "black"
        }
    val pointColor: String = "red"
    val longEdgeColor: String get() = "blue"

//    var shortEdgeColor: String get() {
//
//    }
}

class FilledRectangle : Rectangle() {
    val filler = Filler()

    override fun draw() {
        super.draw()
        println("Filling the rectangle")
    }

    val fillColor: String get() = super.borderColor

    inner class Filler {
        fun fill() {
            println("filling the rectangle through inner class Filler")
        }
        fun drawAndFill() {
            super@FilledRectangle.draw() //calls Rectangle's implementation of draw()
            fill()
            println("drawn a filled rectangle with color ${super@FilledRectangle.borderColor}")
        }
    }
}

interface Polygon {
    fun draw() {
        println("drawing from Polygon interface")
    }
    fun openDraw() {
        println("if a function inside an interface is implemented and not explicitly declared open")
    }
}

class Square() : Rectangle(), Polygon {
    val aValue: Int by lazy {
        println("any amount of processing")
        1
    }
    var bValue: Int by Delegates.observable(1) {
        property, oldValue, newValue ->  println("oldValue = $oldValue, newValue = $newValue, property = $property")
    }
    override fun draw() {
        super<Rectangle>.draw()
        super<Polygon>.draw()
    }

    override fun openDraw() {
        super.openDraw()
    }

//    var cValue: Int by Delegates.vetoable(1) {
//        property, oldValue, newValue ->
//        println("oldValue = $oldValue, newValue = $newValue, property = $property")
//    }
}

abstract class AbstractRectangle : Rectangle() {
    abstract override fun draw()
}

fun main() {
    println()
    val filledRectangle = FilledRectangle()
//    filledRectangle.filler.drawAndFill()
//    filledRectangle.borderColor
//    println()
//    println("filledRectangle.borderColor=${filledRectangle.borderColor}")
//    println("filledRectangle.pointColor=${filledRectangle.pointColor}")

    println()
    val square = Square()
    square.draw()
    println("square.aValue = ${square.aValue}")
    square.openDraw()
    println("square.aValue = ${square.aValue}")
    square.bValue = 4
}