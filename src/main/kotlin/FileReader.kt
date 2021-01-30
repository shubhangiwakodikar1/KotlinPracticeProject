import java.io.FileInputStream

class FileReader {
    val fileName: String = "readableFile"
    fun read() {
        val fileInputStream = FileInputStream(fileName)
        fileInputStream.bufferedReader().lines().forEach() {
            println("next line = ${it}")
        }
    }
}

//FileReadable

fun main() {
    val fileReader = FileReader()
//    fileReader.read("/Users/shubhangiwakodikar/smartthings/bin/code/IntelliJCE2020.2Workspaces/kotlinPracticeWorkspace/readableFile1")
    println()
    fileReader.read()
}