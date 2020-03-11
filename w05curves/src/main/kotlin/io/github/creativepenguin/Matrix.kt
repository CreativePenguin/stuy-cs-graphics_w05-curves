package io.github.creativepenguin

import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

data class Matrix(val cols: Int = 0) {

    private val matrix: Array<MutableList<Double>> = Array(4) { MutableList(cols) { 0.0 } }
    val size = matrix[0].size

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

    fun rotX(theta: Double): Matrix {
        val tmp = Matrix()
        tmp[1][1] = cos(theta)
        tmp[2][2] = cos(theta)
        tmp[2][1] = sin(theta)
        tmp[1][2] = -sin(theta)
        return matrixMult(tmp, this)
    }

    fun rotY(theta: Double): Matrix {
        val tmp = Matrix()
        tmp[0][0] = -sin(theta)
        tmp[2][2] = sin(theta)
        tmp[2][1] = cos(theta)
        tmp[0][2] = cos(theta)
        return matrixMult(tmp, this)
    }

    fun rotZ(theta: Double): Matrix {
        val tmp = Matrix()
        tmp[0][0] = cos(theta)
        tmp[1][1] = cos(theta)
        tmp[0][1] = -sin(theta)
        tmp[1][0] = sin(theta)
        return matrixMult(tmp, this)
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
            this.add(x, y, dz)
        }
    }

    /**
     * @param _step Must be between 0 and 1
     */
    fun addCurve(x0: Double, y0: Double,
                 x1: Double, y1: Double,
                 x2: Double, y2: Double,
                 x3: Double, y3: Double,
                 _step: Double, type: CurveType) {
        when(type) {
            CurveType.BEZIER -> {
                val max = 1 / _step
                for (i in 0..max.toInt()) {
                    val t = 1 / i.toDouble()
                    val x = (-x0 + 3 * x1 - 3 * x2 + x3) * t.pow(3) +
                            (3 * x0 - 6 * x1  + 3 * x2) * t.pow(2) +
                            (-3 * x0 + 3 * x1) * t + x0
                    val y = (-y0 + 3 * y1 - 3 * y2 + y3) * t.pow(3) +
                            (3 * y0 - 6 * y1  + 3 * y2) * t.pow(2) +
                            (-3 * y0 + 3 * y1) * t + y0
                    add(x, y, 1.0)
                }
            }
            CurveType.HERMITE -> {

            }

        }
    }

    fun drawLines(s: BasicBitmapStorage, c: Color) {
        if (size < 2) error("Need at least 2 points to draw line")
        for (point in 0 until size - 1 step 2)
            drawLine(
                matrix[0][point].toInt(), matrix[1][point].toInt(),
                matrix[0][point + 1].toInt(), matrix[0][point + 1].toInt(), s, c)
    }

    operator fun get(index: Int): MutableList<Double> {
        return matrix[index]
    }

    operator fun set(index: Int, value: MutableList<Double>) {
        matrix[index] = value
    }

    operator fun times(m: Matrix): Matrix {
        val tmp = m
        for (row in 0..m.cols) {
            for (col in 0..4) {
                tmp[row][col] = (this[0][col] * m[row][0] +
                        this[1][col] * m[row][1] +
                        this[2][col] * m[row][2] +
                        this[3][col] * m[row][3])
            }
        }
        return tmp
    }

    override fun toString(): String {
        var ans = "[\n"
        for (row in matrix) {
            ans += "\t[ "
            for (col in matrix) {
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
    for (i in 0..4) ans[i][i] = 1.0
    return ans
}


enum class CurveType {
    HERMITE, BEZIER
}

fun matrixMult(m1: Matrix, m2: Matrix): Matrix {
    val tmp = m2
    for (row in 0..m2.cols) {
        for (col in 0..4) {
            m2[row][col] = (m1[0][col] * tmp[row][0] +
                    m1[1][col] * tmp[row][1] +
                    m1[2][col] * tmp[row][2] +
                    m1[3][col] * tmp[row][3])
        }
    }
    return m2
}
