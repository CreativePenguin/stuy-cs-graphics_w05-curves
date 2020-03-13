package io.github.creativepenguin

import java.awt.Color
import java.io.File
import java.lang.IllegalArgumentException

private fun readFileAsTextUsingInputStream(fname: String) =
    File(fname).inputStream().readBytes().toString(Charsets.UTF_8)

private fun readFileAsLinesUsingBufferedReader(fname: String): List<String> =
    File(fname).bufferedReader().readLines()

enum class ParseModes {
    LINE, MOVE, ROTATE, CIRCLE, HERMITE, BEZIER, DISPLAY, CLEAR, IDENT, SAVE
}

enum class Colors(val value: Color) {
    RED(Color.RED), BLUE(Color.BLUE), GREEN(Color.GREEN),
    BLACK(Color.BLACK), WHITE(Color.WHITE)
}

fun parseFile(fname: String, transform: Matrix, edges: Matrix, s: BasicBitmapStorage) {
    val buffer = readFileAsLinesUsingBufferedReader(fname).iterator()
    for (line in buffer) {
        try {
            when (ParseModes.valueOf(line.toUpperCase())) {
                ParseModes.LINE -> {
                    val points = buffer.next().split(" ")
                    val p: MutableList<Double> = mutableListOf()
                    points.forEach { p.add(it.toDouble()) }
                    val (x0, y0, z0) = p.slice(0..2)
                    val (x1, y1, z1) = p.slice(3..5)
                    edges.add(x0, y0, z0)
                    edges.add(x1, y1, z1)
                }
                // TODO: Restructure these things to use tuples
                ParseModes.CIRCLE -> {
                    val points = buffer.next().split(" ")
                    val (cx, cy, dz, r) = points
                    edges.addCircle(
                        cx.toDouble(), cy.toDouble(), dz.toDouble(), r.toDouble(), .01
                    )
                }
                ParseModes.BEZIER -> {
                    val p = buffer.next().split(" ")
                    edges.addCurve(
                        p[0].toDouble(), p[1].toDouble(),
                        p[2].toDouble(), p[3].toDouble(),
                        p[4].toDouble(), p[5].toDouble(),
                        p[6].toDouble(), p[7].toDouble(),
                        .01, CurveType.BEZIER
                    )
                }
                ParseModes.HERMITE -> {
                    val p = buffer.next().split(" ")
                    edges.addCurve(
                        p[0].toDouble(), p[1].toDouble(),
                        p[2].toDouble(), p[3].toDouble(),
                        p[4].toDouble(), p[5].toDouble(),
                        p[6].toDouble(), p[7].toDouble(),
                        .01, CurveType.HERMITE
                    )
                }
                ParseModes.CLEAR -> s.clear()
                ParseModes.DISPLAY -> {
                    val color = Colors.valueOf(buffer.next().toUpperCase())
                    println(edges)
                    edges.drawLines(s, color.value)
//                    (edges * transform).drawLines(s, color.value)
                }
                ParseModes.SAVE -> s.savePPM(buffer.next())
                else -> {
                    error("$line is not implemented yet whoops")
                }
            }
        } catch (e: IllegalArgumentException) {
            if (line[0] == '#') {
                continue
            } else {
                throw e
            }
        }
    }
}
