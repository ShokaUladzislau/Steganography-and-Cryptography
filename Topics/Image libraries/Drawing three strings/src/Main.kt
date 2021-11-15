import java.awt.Color
import java.awt.image.BufferedImage

fun drawStrings(): BufferedImage {
    val height = 200
    val width = 200
    val image = BufferedImage(height, width, BufferedImage.TYPE_INT_RGB)
    val message = "Hello, images!"

    val graphicsRED = image.createGraphics()
    graphicsRED.color = Color.RED
    graphicsRED.drawString(message, 50, 50)

    val graphicsGREEN = image.createGraphics()
    graphicsGREEN.color = Color.GREEN
    graphicsGREEN.drawString(message, 51, 51)

    val graphicsBLUE = image.createGraphics()
    graphicsBLUE.color = Color.BLUE
    graphicsBLUE.drawString(message, 52, 52)

    return image
}
