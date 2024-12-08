data class Equation(val result: Long, val inputs: List<Long>)


fun solve(eq: Equation): Boolean {
    fun go(pos: Int, remainder: Long): Boolean {

        if (pos < 0) {
            return remainder == 0L
        }
        if (remainder <= 0) {
            return false
        }

        val whole = remainder.toString()
        val cur = eq.inputs[pos]

        if (remainder % cur == 0L) {
            if (go(pos-1, remainder / cur)) {
                return true
            }
        }
        if (remainder==cur) {
            return pos == 0
        }

        val suffix = cur.toString()
        if (whole.endsWith(suffix)) {
            val newRemainder = whole.dropLast(suffix.length)
            if (go(pos-1, newRemainder.toLong())) {
                return true
            }
        }

        return go(pos-1,remainder- cur)
    }
    return go(eq.inputs.size-1, eq.result)
}



fun main() {
    val day01 = getResourceAsText("Day07.txt")!!
    val inp0 = day01.lines().filter{ it.trim().isNotEmpty() }.map {
        val (l,r) = it.split(Regex(":\\s+"))
        Equation(l.toLong(), r.split(Regex("\\s+")).map{it.toLong()})
    }
     println( inp0.asSequence().filter { solve(it)}.map{ it.result }.sum())

}