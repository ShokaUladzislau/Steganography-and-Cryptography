package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

fun main() {
    while (true) {
        when (val input = prompt("Task (hide, show, exit):")) {
            "hide" -> hideMessage()
            "show" -> showMessage()
            "exit" -> {
                println("Bye!"); break
            }
            else -> println("Wrong task: $input")
        }
    }
}

fun prompt(text: String, sameLine: Boolean = false): String {
    if (sameLine) print(text) else println(text)
    return readLine() ?: ""
}

fun hideMessage() {
    val inputFilePath = prompt("Input image file:", false)
    val outputFilePath = prompt("Output image file:", false)
    val messageToHide = prompt("Message to hide:", false)
    val passwordToHide = prompt("Password:", false)

    try {
        val inputFile = File(inputFilePath)
        println("Input Image: ${inputFile.path}")
        val outputFile = File(outputFilePath)
        println("Output Image: ${outputFile.path}")

        val image = ImageIO.read(inputFile)
        val messageWithPassword = encryptMessage(messageToHide, passwordToHide).toEncodedMessage()
        image.encodeBytes(messageWithPassword)

        ImageIO.write(image, inputFile.extension, outputFile)
        check(outputFile.exists())
        println("Message saved in ${outputFile.path} image.")
    } catch (e: IOException) {
        println("Can't read input file!")
    }
}

fun showMessage() {
    val inputFilePath = prompt("Input image file:", false)
    val passwordToHide = prompt("Password:", false)
    val decodedMessage = ImageIO.read(File(inputFilePath)).decodeMessage()
    val message = encryptMessage(decodedMessage, passwordToHide)
    println("Message:\n$message")
}

fun BufferedImage.decodeMessage(): String {
    return pixelRowCols()
        .map { (row, col) ->
            Color(this.getRGB(col, row)).blue and 1
        }
        .collectBitsToBytes()
        .takeWhileWindow(3) { it != listOf(0, 0, 3) }
        .map { (it).toChar() }
        .joinToString("")
}

fun Sequence<Int>.collectBitsToBytes(): Sequence<Int> {
    return chunked(8)
        .map { bits ->
            bits.asReversed()
                .mapIndexed { index, n -> n shl index }
                .reduce { acc, n -> acc or n }
        }
}

fun <T> Sequence<T>.takeWhileWindow(size: Int, predicate: (List<T>) -> Boolean): Sequence<T> {
    return this.windowed(size)
        .takeWhile(predicate)
        .map { it.first() }
}

fun String.toEncodedMessage(): ByteArray {
    return encodeToByteArray() + byteArrayOf(0, 0, 3)
}

fun BufferedImage.encodeBytes(bytes: ByteArray) {
    if (!imageCanFitEncodedBytes(this, bytes)) {
        println("The input image is not large enough to hold this message.")
    } else {
        val pixelLocations = pixelRowCols().iterator()
        for (msgCharacter in bytes.map { it.toInt() }) {
            for (bitPosition in 7 downTo 0) {
                val (row, col) = pixelLocations.next()
                val bit = (msgCharacter shr bitPosition) and 1
                encodeBitInPixel(row, col, bit)
            }
        }
    }
}

fun imageCanFitEncodedBytes(image: BufferedImage, bytes: ByteArray): Boolean {
    return bytes.size * 8 < image.height * image.width
}

fun BufferedImage.pixelRowCols() = sequence {
    for (row in 0 until height) {
        for (col in 0 until width) {
            yield(row to col)
        }
    }
}

fun BufferedImage.encodeBitInPixel(row: Int, col: Int, bit: Int) {
    val color = Color(this.getRGB(col, row))
    val newColor = Color(
        color.red,
        color.green,
        color.blue.withEndingBit(bit)
    )
    this.setRGB(col, row, newColor.rgb)
}

fun Int.withEndingBit(value: Int): Int {
    return if (this and 0b1 != value) this xor 0b1 else this
}

fun encryptMessage(message: String, password: String): String {

    val binaryPassword = password.map { it }.joinToString(separator = "")
    val binaryMessage = message.map { it }.joinToString(separator = "")

    val extendedPassword =
        binaryPassword.repeat(binaryMessage.length / binaryPassword.length) + binaryPassword.take(binaryMessage.length % binaryPassword.length)

    return binaryMessage xor extendedPassword
}

infix fun String.xor(that: String) = mapIndexed { index, c ->
    that[index].code.xor(c.code)
}.joinToString(separator = "") {
    it.toChar().toString()
}
