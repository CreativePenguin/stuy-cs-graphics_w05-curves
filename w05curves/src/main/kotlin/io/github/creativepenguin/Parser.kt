package io.github.creativepenguin

import java.io.File
import java.lang.IllegalArgumentException

private fun readFileAsTextUsingInputStream(fname: String) =
    File(fname).inputStream().readBytes().toString(Charsets.UTF_8)

private fun readFileAsLinesUsingBufferedReader(fname: String): List<String> =
    File(fname).bufferedReader().readLines()

enum class ParseModes {
    LINE, MOVE, ROTATE, CIRCLE, HERMITE, BEZIER, IDENT, DISPLAY
}

fun parseFile(fname: String, transform: Matrix, edges: Matrix, s: BasicBitmapStorage) {
    var parsemodes = ParseModes.NONE
    val buffer = readFileAsLinesUsingBufferedReader(fname).iterator()
    for (line in buffer) {
        try {
            when (ParseModes.valueOf(line.toUpperCase())) {

                ParseModes.LINE -> {
                    val p = buffer.next().split(" ").toList<Double>
                    edges.drawLine(p[0].toDouble())
                }

                //TODO: Restructure these things to use tuples
                ParseModes.CIRCLE -> {
                    val coordinates = buffer.next().split(" ")
//                    coordinates.forEach { it.toDouble() }
                    edges.addCircle(
                        coordinates[0].toDouble(), coordinates[1].toDouble(),
                        coordinates[2].toDouble(), coordinates[3].toDouble(), .05
                    )
                }

                ParseModes.BEZIER -> {
                    val p = buffer.next().split(" ")
                    edges.addCurve(
                        p[0].toDouble(), p[1].toDouble(),
                        p[2].toDouble(), p[3].toDouble(),
                        p[4].toDouble(), p[5].toDouble(),
                        p[6].toDouble(), p[7].toDouble(),
                        .05, CurveType.BEZIER
                    )
                }

                else -> {
                    println("Not implemented yet whoops")
                }
            }
        } catch(e: IllegalArgumentException) {
            if(line[0] == '#') {
                continue
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}
