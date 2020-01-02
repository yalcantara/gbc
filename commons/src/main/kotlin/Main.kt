import com.gbc.commons.structs.Grid

fun main(args: Array<String>){
    val g = Grid(3, 3)

    g.header(0, "Pop")
    g[0, 0] = 7
    g[0, 1] = 7.1
    g[0, 1] = 72325656763.232343
    g[1,1] = Double.NaN
    g[2, 2] = 1/2.0
    g[1, 2] = "sdjaksdsads sdsa    "
    g.print()
}