import java.awt.Color
import java.awt.image.BufferedImage

fun drawLines(): BufferedImage {
    val height = 200
    val width = 200
    val image = BufferedImage(height, width, BufferedImage.TYPE_INT_RGB)

    val graphicsRED = image.createGraphics()
    graphicsRED.color = Color.RED
    graphicsRED.drawLine(0, 0, 200, 200)

    val graphicsGREEN = image.createGraphics()
    graphicsGREEN.color = Color.GREEN
    graphicsGREEN.drawLine(200, 0, 0, 200)

    return image
}
