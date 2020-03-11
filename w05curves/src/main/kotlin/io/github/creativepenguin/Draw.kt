package io.github.creativepenguin

import java.awt.Color
import kotlin.math.abs

fun drawLine(
    _x0: Int,
    _y0: Int,
    _x1: Int,
    _y1: Int,
    s: BasicBitmapStorage,
    c: Color
) {
    // swap points if going right -> left
    val xt: Int
    val yt: Int
    var d: Int
    var (x0, y0, x1, y1) = arrayOf(_x0, _y0, _x1, _y1)

    if (x0 > x1) {
        xt = x0
        yt = y0
        x0 = x1
        y0 = y1
        x1 = xt
        y1 = yt
    }

    var x = x0
    var y = y0
    val A = 2 * (y1 - y0)
    val B = -2 * (x1 - x0)

    // octants 1 and 8
    if (abs(x1 - x0) >= abs(y1 - y0)) {

        // octant 1
        if (A > 0) {

            d = A + B / 2
            while (x < x1) {
                s.setPixel(x, y, c)
                if (d > 0) {
                    y += 1
                    d += B
                }
                x++
                d += A
            } // end octant 1 while
            s.setPixel(x1, y1, c)
        } // end octant 1

        // octant 8
        else {
            d = A - B / 2

            while (x < x1) {
                // printf("(%d, %d)\n", x, y);
                s.setPixel(x, y, c)
                if (d < 0) {
                    y -= 1
                    d -= B
                }
                x++
                d += A
            } // end octant 8 while
            s.setPixel(x1, y1, c)
        } // end octant 8
    } // end octants 1 and 8

    // octants 2 and 7
    else {

        // octant 2
        if (A > 0) {
            d = A / 2 + B

            while (y < y1) {
                s.setPixel(x, y, c)
                if (d < 0) {
                    x += 1
                    d += A
                }
                y++
                d += B
            } // end octant 2 while
            s.setPixel(x1, y1, c)
        } // end octant 2

        // octant 7
        else {
            d = A / 2 - B

            while (y > y1) {
                s.setPixel(x, y, c)
                if (d > 0) {
                    x += 1
                    d += A
                }
                y--
                d -= B
            } // end octant 7 while
            s.setPixel(x1, y1, c)
        } // end octant 7
    } // end octants 2 and 7
}
