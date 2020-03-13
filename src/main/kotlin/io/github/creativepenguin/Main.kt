package io.github.creativepenguin

fun main() {
    val screen = BasicBitmapStorage(500, 500)
    val edges = Matrix()
    val transform = ident()
//    edges.add(125.0, 37.0, 15.0)
//    edges.add(160.0, 343.0, 125.0)
//    println(edges)
//    println(edges.size)
//    edges.drawLines(screen, Color.WHITE)
    parseFile("src/main/resources/script", transform, edges, screen)
//    screen.savePPM("src/main/resources/img.ppm")
}
