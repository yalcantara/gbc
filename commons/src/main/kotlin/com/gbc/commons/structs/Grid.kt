package com.gbc.commons.structs

import com.gbc.commons.lang.toList
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.math.max


class Grid {

    companion object {
        private val MAX_PRINT_ROWS = 200
        private val MAX_PRINT_COLS = 50
        private val MAX_DISPLAY_LENGTH = 60

        fun wrap(values: List<Map<*, *>>): Grid {

            val colNames = LinkedHashSet<String>()

            for (m: Map<*, *> in values) {
                for ((idx, key) in m.keys.withIndex()) {
                    val name = if (key == null) "Column $idx" else key.toString()
                    colNames.add(name)
                }
            }

            val rows = values.size
            val cols = colNames.size

            val g = Grid(rows, cols)

            for ((c, col) in colNames.withIndex()) {
                g.header(c, col)
            }

            for (i in 0 until values.size) {

                val m: Map<*, *> = values[i]
                for ((j, n) in colNames.withIndex()) {

                    if (m.containsKey(n)) {
                        val value = m[n]

                        g.set(i, j, value)
                    }
                }
            }

            return g
        }

        private fun initHeader(cols:Int):Array<String>{
            return Array<String>(cols) { i -> "Column $i"}
        }

        private fun initData(rows: Int, cols: Int):Array<Array<String?>>{
            return Array<Array<String?>>(rows) { Array<String?>(cols) { null} }
        }
    }


    val rows: Int
    val cols: Int

    private var headerAssigned = false
    private val header:Array<String>
    private val data:Array<Array<String?>>

    constructor(rows: Int, cols:Int){
        this.rows = rows
        this.cols = cols
        header = initHeader(cols)
        data = initData(rows, cols)
    }
    constructor(values:List<String>) {
        this.rows = values.size
        this.cols = 1
        header = initHeader(cols)
        data = initData(rows, cols)
        for ((i, v) in values.withIndex()){
            set(i, 0, v)
        }
    }

	constructor(values:Set<String>) {
        this.rows = values.size
        this.cols = 1
        header = initHeader(cols)
        data = initData(rows, cols)
        for ((i, v) in values.withIndex()){
            set(i, 0, v)
        }
    }

    constructor(values:Iterable<String>):this(toList(values)) {
    }

    fun header(j:Int):String {
        checkColIndex(j)
        return header[j]
    }

    fun header(j:Int, title:String) {
        checkColIndex(j)
        header[j] = title
        headerAssigned = true
    }

	 operator fun get(i:Int, j:Int):String? {
         checkIndex(i, j)
         return data[i][j]
    }

    operator fun set(i:Int, j:Int, value:Any?) {
        checkIndex(i, j)
        if (value == null) {
            data[i][j] = null
            return
        }

        if (value is String){
            data[i][j] = value
            return
        }

        if (value is Number) {
            data[i][j] = format (value)
            return
        }

        data[i][j] = value.toString()
    }


	private fun checkIndex(i:Int, j:Int) {
        checkRowIndex(i)
        checkColIndex(j)
    }

	private fun checkRowIndex(i:Int) {
        if (i < 0) {
            throw IndexOutOfBoundsException("The row index can not be less than 0. Got: $i.")
        }

        if (i >= rows) {
            throw IndexOutOfBoundsException(
            "The row index is out of bounds. Expected a value less than $rows, but got: $i instead.")
        }
    }

	private fun checkColIndex(j:Int) {
        if (j < 0) {
            throw IllegalArgumentException("The col index can not be less than 0. Got: $j.")
        }

        if (j >= cols) {
            throw IndexOutOfBoundsException(
            "The column index is out of bounds. Expected a value less than $cols, but got: $j instead.")
        }
    }

    fun print() {
        print(toString())
    }

    fun print(maxRows:Int, maxCols:Int) {
        print(toString(maxRows, maxCols))
    }

	override fun toString():String {
        return toString(MAX_PRINT_ROWS, MAX_PRINT_COLS)
    }

    fun toString(maxRows:Int, maxCols:Int):String {
        val sb = StringBuilder(1024)
        print(sb, maxRows, maxCols)
        return sb.toString()
    }

	private fun format(n:Number):String {
        if (n is Double) {
            if (n.isNaN()) {
                return "(NaN)"
            }
            if (n.isInfinite() && n < 0) {
                return "(-Infinite)"
            }
            if(n.isInfinite()){
                return "(Infinite)"
            }
        }

        if (n is Float) {
            if (n.isNaN()) {
                return "(NaN)"
            }

            if (n.isInfinite() && n < 0) {
                return "(-Infinite)"
            }
            if(n.isInfinite()){
                return "(Infinite)"
            }
        }
        val nf = DecimalFormat()
        nf.minimumFractionDigits = 0
        nf.maximumFractionDigits = 10

        return nf.format(n)
    }

	private fun trimForPrint(str:String?):String {
        if (str == null) {
            return "(null)"
        }

        val ans = str.trim()
        if (ans.length > MAX_DISPLAY_LENGTH) {
            return ans.substring(0, MAX_DISPLAY_LENGTH)
        }
        return ans
    }

	private fun print(out:Appendable, maxRows:Int, maxCols:Int) {
        print(out, maxRows, maxCols, headerAssigned)
    }

	private fun print(out:Appendable, maxRows:Int, maxCols:Int, includeHeaders:Boolean) {
        val mr = if (maxRows < rows) maxRows else rows
        val mc = if (maxCols < cols) maxCols else cols

        try {
            if (mr < rows || mc < cols) {
                out.append("Grid " + rows + "x" + cols + "  (truncated)\n")
            } else {
                out.append("Grid " + rows + "x" + cols + "\n")
            }

            val maxLength = IntArray(mc)

            //Computing max length of the fields
            //======================================================================
            for (j in 0 until mc) {
                val str = trimForPrint(header(j))
                maxLength[j] = max(maxLength[j], str.length)
            }

            for (j in 0 until mc) {
                for (i in 0 until mr) {
                    val str = trimForPrint(this[i, j])
                    maxLength[j] = max(maxLength[j], str.length)
                }
            }
            //======================================================================

            if (includeHeaders) {
                // Headers
				// =================================================================
				line(out, maxLength, mc)
                out.append('\n')
                for (j in 0 until mc) {
                    printRow(out, maxLength, j) { -> trimForPrint(header(j))}
                }
                // =================================================================
				out.append('\n')
            }
            line(out, maxLength, mc)
            out.append('\n')

            for (i in 0 until mr) {
                for (j in 0 until mc) {
                    printRow(out, maxLength, j){ -> trimForPrint(this[i, j])}
                }
                out.append("\n")
            }
            line(out, maxLength, mc)
            out.append("\n")
        }  catch (e:IOException) {
            throw RuntimeException(e)
        }
    }

    fun printRow(out:Appendable, maxLength:IntArray, j:Int, supplier: ()->String){
        if (j == 0) {
            out.append("| ")
        } else {
            out.append(" ")
        }
        val h = trimForPrint(supplier())
        val leading = maxLength[j] - h.length

        for (s in 0 until leading) {
            out.append(" ")
        }

        out.append(h)
        out.append(" |")
    }

	@Throws(IOException::class)
    private fun line(out:Appendable, maxLength:IntArray, maxCols:Int) {

        for (j in 0 until maxCols) {
            if (j == 0) {
                out.append("+-")
            } else {
                out.append("-")
            }

            for (s in 0 until maxLength[j]) {
                out.append("-")
            }

            out.append("-+")
        }
    }
}