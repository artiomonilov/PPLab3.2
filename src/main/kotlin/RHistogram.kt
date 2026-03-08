import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Value
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object RHistogram {
    private const val WIDTH = 1000
    private const val HEIGHT = 500

    private fun <E> WriteImage(showPlot: Value, frame: String?, values: Array<E?>?) {
        val image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)
        val graphics = image.getGraphics() as Graphics2D

        graphics.setBackground(Color(255, 255, 255))
        graphics.clearRect(0, 0, WIDTH, HEIGHT)

        showPlot.execute(graphics, WIDTH, HEIGHT, values)

        try {
            ImageIO.write(image, "png", File(frame + ".png"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        println("Saved [" + frame + ".png]")
    }

    fun BuildHistogram(values: Array<Any?>?, frame: String?, how: Boolean) {
        val context: Context = Context.newBuilder("R").allowAllAccess(true).build()
        val src: String?

        if (how) {
            src = "library(lattice)\n" +
                    "function(graphics, width, height, values) {\n" +
                    "  grDevices:::awtrealize(graphics, width, height)\n" +
                    "  print(barchart(table(values), horizontal=FALSE))\n" +
                    "  grDevices:::awtfinish()\n" +
                    "}"
        } else {
            src = "library(lattice)\n" +
                    "function(graphics, width, height, values) {\n" +
                    "  grDevices:::awtrealize(graphics, width, height)\n" +
                    "  print(xyplot(values ~ seq_along(values)))\n" +
                    "  grDevices:::awtfinish()\n" +
                    "}"
        }

        val showPlot: Value = context.eval("R", src)
        WriteImage<Any?>(showPlot, frame, values)
        context.close()
    }
}