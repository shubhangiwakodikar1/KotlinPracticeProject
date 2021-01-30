class FirstKotlinClass(value: String) {
    var propertyName: String? = value
        get() {
            return "propertyName"
        }
        set(valuePassed) {
            field = valuePassed
        }

    var intProperty = 1
}

fun main() {

}