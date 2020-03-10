package io.github.creativepenguin

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

enum class CurveType {
    HERMITE, BEZIER
}

fun addCircle(points: Matrix, cx: Int, cy: Int, dz: Int, r: Int, step: Int) {
    val x = r * cos(2 * PI * step) + cx
    val y = r * sin(2 * PI * step) + cy
}

fun addCurve(points: Matrix, x0: Int, y0: Int, x1: Int, y1: Int, x2: Int, y2: Int, x3: Int, y3: Int, step: Int, curveType: CurveType) {
    
}
