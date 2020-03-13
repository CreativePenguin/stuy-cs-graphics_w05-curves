package io.github.creativepenguin

import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

data class Matrix(val cols: Int = 0) {

    private var matrix: Array<MutableList<Double>> = Array(4) { MutableList(cols) { 0.0 } }
    val size
        get() = matrix[0].size

    fun translate(x: Int, y: Int, z: Int) {
        for (i in 0..cols) {
            matrix[0][i] = matrix[0][i] + x
            matrix[1][i] = matrix[1][i] + y
            matrix[2][i] = matrix[2][i] + z
        }
    }

    fun scale(x: Int, y: Int, z: Int) {
        for (i in 0..cols) {
            matrix[0][i] = matrix[0][i] * x
            matrix[1][i] = matrix[1][i] * y
            matrix[2][i] = matrix[2][i] * z
        }
    }

    fun rotX(theta: Double) {
        val tmp = ident()
        tmp[1, 1] = cos(theta)
        tmp[2, 2] = cos(theta)
        tmp[2, 1] = sin(theta)
        tmp[1, 2] = -sin(theta)
        matrix = matrixMult(tmp, matrix)
    }

    fun rotY(theta: Double) {
        val tmp = ident()
        tmp[0, 0] = -sin(theta)
        tmp[2, 2] = sin(theta)
        tmp[2, 1] = cos(theta)
        tmp[0, 2] = cos(theta)
        matrix = matrixMult(tmp, matrix)
    }

    fun rotZ(theta: Double) {
        val tmp = Matrix()
        tmp[0, 0] = cos(theta)
        tmp[1, 1] = cos(theta)
        tmp[0, 1] = -sin(theta)
        tmp[1, 0] = sin(theta)
        matrix = matrixMult(tmp, matrix)
    }

    fun add(x: Double, y: Double, z: Double) {
        matrix[0].add(x)
        matrix[1].add(y)
        matrix[2].add(z)
        matrix[3].add(1.0)
    }

    fun addCircle(cx: Double, cy: Double, dz: Double, r: Double, _step: Double) {
        val max = 1 / _step
        for (i in 0..max.toInt()) {
            val t = i.toDouble()
            val x = r * cos(2 * PI * 1 / t) + cx
            val y = r * sin(2 * PI * 1 / t) + cy
            if(t.isNaN() or x.isNaN() or y.isNaN()) continue
            this.add(x, y, dz)
        }
    }

    /**
     * @param _step Must be between 0 and 1
     */
    fun addCurve(
        x0: Double,
        y0: Double,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        x3: Double,
        y3: Double,
        _step: Double,
        type: CurveType
    ) {
        val step = if (_step < 1) 1 / _step else _step
        when (type) {
            CurveType.BEZIER -> {
                for (i in 0..step.toInt()) {
                    val t = 1.0 / i
                    val x = (-x0 + 3 * x1 - 3 * x2 + x3) * t.pow(3) +
                            (3 * x0 - 6 * x1 + 3 * x2) * t.pow(2) +
                            (-3 * x0 + 3 * x1) * t + x0
                    val y = (-y0 + 3 * y1 - 3 * y2 + y3) * t.pow(3) +
                            (3 * y0 - 6 * y1 + 3 * y2) * t.pow(2) +
                            (-3 * y0 + 3 * y1) * t + y0
                    add(x, y, 1.0)
                }
            }
            CurveType.HERMITE -> {
                for (i in 0..step.toInt()) {
                    val t = 1.0 / i
                    val x = (2 * x0 - 2 * x3 + (x1 - x0) + (x3 - x2)) * t.pow(3) +
                            (-3 * x0 + 3 * x3 - 2 * (x1 - x0) - (x3 - x2)) * t.pow(2) +
                            (x1 - x0) + x0
                    val y = (2 * y0 - 2 * y3 + (y1 - y0) + (y3 - y2)) * t.pow(3) +
                            (-3 * y0 + 3 * y3 - 2 * (y1 - y0) - (y3 - y2)) * t.pow(2) +
                            (y1 - y0) + y0
                    add(x, y, 1.0)
                }
            }
        }
    }

    fun drawLines(s: BasicBitmapStorage, c: Color) {
        if (size < 2) error("Need at least 2 points to draw line")
        for (point in 0 until size - 1 step 2) {
            println("{ ${matrix[0][point]}, ${matrix[1][point]}")
            println("${matrix[0][point + 1]}, ${matrix[1][point + 1]} }")
            drawLine(
                matrix[0][point].toInt(), matrix[1][point].toInt(),
                matrix[0][point + 1].toInt(), matrix[1][point + 1].toInt(), s, c
            )
        }
    }

    operator fun get(x: Int, y: Int): Double {
        return matrix[x][y]
    }

    operator fun set(x: Int, y: Int, t: Double) {
        matrix[x][y] = t
    }

    operator fun times(m: Matrix): Matrix {
        for (row in 0..m.cols) {
            for (col in 0..4) {
                m[row, col] = (this[0, col] * m[row, 0] +
                        this[1, col] * m[row, 1] +
                        this[2, col] * m[row, 2] +
                        this[3, col] * m[row, 3])
            }
        }
        return m
    }

    override fun toString(): String {
        var ans = "[\n"
        for (row in matrix) {
            ans += "\t[ "
            for (col in row) {
                ans += "$col "
            }
            ans += "]\n"
        }
        ans += "]"
        return ans
    }
}

fun ident(): Matrix {
    val ans = Matrix(4)
    for (i in 0..3) ans[i, i] = 1.0
    return ans
}

enum class CurveType {
    HERMITE, BEZIER
}

private fun matrixMult(m1: Matrix, m2: Array<MutableList<Double>>): Array<MutableList<Double>> {
    for (row in 0..m2[0].size) {
        for (col in 0..4) {
            m2[row][col] = (m1[0, col] * m2[row][0] +
                    m1[1, col] * m2[row][1] +
                    m1[2, col] * m2[row][2] +
                    m1[3, col] * m2[row][3])
        }
    }
    return m2
}
