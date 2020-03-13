package io.github.creativepenguin

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileOutputStream
import javax.swing.JFrame
import kotlin.math.abs

class BasicBitmapStorage(val width: Int, val height: Int) {
    val image = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

    fun setPixel(_x: Int, _y: Int, c: Color) {
        val (x, y) = Pair(abs(_x), abs(_y))
        image.setRGB(x, y, c.rgb)
    }

    private fun getPixel(x: Int, y: Int) = Color(image.getRGB(x, y))

    fun savePPM(fname: String) {
        val fos = FileOutputStream(fname)
        val buffer = ByteArray(width * 3) // write one line at a time
        fos.use {
            val header = "P6\n$width $height\n255\n".toByteArray()
            with(it) {
                write(header)
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val c = getPixel(x, y)
                        buffer[x * 3] = c.red.toByte()
                        buffer[x * 3 + 1] = c.green.toByte()
                        buffer[x * 3 + 2] = c.blue.toByte()
                    }
                    write(buffer)
                }
            }
        }
    }

    fun clear() = image.graphics.clearRect(0, 0, width, height)
}

fun display(img: BasicBitmapStorage) {
    val frame = JFrame()
//    frame.add(img)
}
