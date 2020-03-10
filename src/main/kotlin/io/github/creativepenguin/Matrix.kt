package io.github.creativepenguin

import kotlin.math.cos
import kotlin.math.sin

data class Matrix(val rows: Int = 4, val cols: Int = 4) {

    val matrix: Array<Array<Double>> = Array(rows) { Array(cols) {0.0} }

    init {
        matrix[0][0] = 1.0
        matrix[1][1] = 1.0
        matrix[2][2] = 1.0
        matrix[3][3] = 1.0
    }

    fun makeTranslate(x: Int, y: Int, z: Int) {
        for(i in 0..cols) {
            matrix[0][i] = matrix[0][i] + x
            matrix[1][i] = matrix[1][i] + y
            matrix[2][i] = matrix[2][i] + z
        }
    }

    fun makeScale(x: Int, y: Int, z: Int) {
        for(i in 0..cols) {
            matrix[0][i] = matrix[0][i] * x
            matrix[1][i] = matrix[1][i] * y
            matrix[2][i] = matrix[2][i] * z
        }
    }

    fun makeRotX(theta: Double): Matrix {
        val tmp = Matrix()
        tmp[1][1] = cos(theta)
        tmp[2][2] = cos(theta)
        tmp[2][1] = sin(theta)
        tmp[1][2] = -sin(theta)
        return matrixMult(tmp, this)
    }

    fun makeRotY(theta: Double): Matrix {
        val tmp = Matrix()
        tmp[0][0] = -sin(theta)
        tmp[2][2] = sin(theta)
        tmp[2][1] = cos(theta)
        tmp[0][2] = cos(theta)
        return matrixMult(tmp, this)
    }

    fun makerotZ(theta: Double): Matrix {
        val tmp = Matrix()
        tmp[0][0] = cos(theta)
        tmp[1][1] = cos(theta)
        tmp[0][1] = -sin(theta)
        tmp[1][0] = sin(theta)
        return matrixMult(tmp, this)
    }

    operator fun get(index: Int):Array<Double> {
        return matrix[index]
    }

    operator fun set(index:Int, value: Array<Double>) {
        matrix[index] = value
    }

    operator fun times(m: Matrix):Matrix {
        val tmp = m
        for(row in 0..m.rows) {
            for(col in 0..4) {
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
        for(row in matrix) {
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

fun matrixMult(m1: Matrix, m2: Matrix): Matrix {
    val tmp = m2
    for(row in 0..m2.rows) {
        for(col in 0..4) {
            m2[row][col] = (m1[0][col] * tmp[row][0] +
                    m1[1][col] * tmp[row][1] +
                    m1[2][col] * tmp[row][2] +
                    m1[3][col] * tmp[row][3])
        }
    }
    return m2
}
