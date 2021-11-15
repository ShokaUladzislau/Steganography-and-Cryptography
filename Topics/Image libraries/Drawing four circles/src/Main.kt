import java.awt.Color
import java.awt.image.BufferedImage

fun drawCircles(): BufferedImage {
    val height = 200
    val width = 200
    val image = BufferedImage(height, width, BufferedImage.TYPE_INT_RGB)

    val graphicsRED = image.createGraphics()
    graphicsRED.color = Color.RED
    graphicsRED.drawOval(50, 50, 100, 100)

    val graphicsYELLOW = image.createGraphics()
    graphicsYELLOW.color = Color.YELLOW
    graphicsYELLOW.drawOval(50, 75, 100, 100)

    val graphicsGREEN = image.createGraphics()
    graphicsGREEN.color = Color.GREEN
    graphicsGREEN.drawOval(75, 50, 100, 100)

    val graphicsBLUE = image.createGraphics()
    graphicsBLUE.color = Color.BLUE
    graphicsBLUE.drawOval(75, 75, 100, 100)

    return image
}
