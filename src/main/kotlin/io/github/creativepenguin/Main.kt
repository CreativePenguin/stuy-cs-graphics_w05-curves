package io.github.creativepenguin

import java.awt.Color

fun main() {
    val screen = BasicBitmapStorage(1000, 1000)
    val edges = Matrix()
    val transform = ident()
//    edges.add(125.0, 37.0, 15.0)
//    edges.add(160.0, 343.0, 125.0)
//    println(edges)
//    println(edges.size)
//    edges.drawLines(screen, Color.WHITE)
    parseFile("src/main/resources/script", transform, edges, screen)
    screen.savePPM("src/main/resources/img.ppm")
}
