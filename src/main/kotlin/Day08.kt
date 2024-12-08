typealias Point = Pair<Int, Int>

fun parseGrid(s: String): Map<Char, List<Point>> =
    s.lines().asSequence().flatMapIndexed { y: Int,ln: String ->
      ln.mapIndexed { x,chr -> chr to Pair(x,y)}
    }
    .filter { it.first != '.' }
    .groupBy({ it.first}, {it.second})

operator fun Point.minus(other: Point): Point = Point(this.first - other.first, this.second - other.second)
operator fun Point.plus(other: Point): Point = Point(this.first + other.first, this.second + other.second)
fun gcd(l0: Int, r0: Int): Int {

    var l=l0
    var r = r0
    if (l < r) {
        val x = l
        l = r
        r = x
    }
    while (r != 0) {
        val m = l.rem(r)
        l = r
        r = m
    }
    return l
}
fun Point.norm(): Point {
    val common = gcd(first, second)
    return Point(first/common, second/common)
}
fun candidates(x: Point, y: Point): List<Point>  {
    val delta = y - x
    return listOf(x - delta, y + delta)
}
fun candidates2(x: Point, y: Point, b: Bounds) = sequence {
    val delta = (y - x).norm()
    yieldAll(generateSequence(x) { it - delta }.takeWhile(b::inBounds))
    yield(x)
    yieldAll(generateSequence(x) { it + delta }.takeWhile(b::inBounds))
}
data class Bounds(val width: Int, val height: Int) {
    fun inBounds(pos: Point) = pos.first in 0..<width && pos.second in 0..<height
}

fun <A> List<A>.allPairs(): Sequence<Pair<A,A>> = sequence {
    for (i in indices) {
        for (j in i+1..<size) {
            yield(Pair(get(i), get(j)))
        }
    }

}

fun pointsFor(points: List<Point>): Set<Point> = points.allPairs().flatMap { (x,y)-> candidates(x,y) }.toSet()
fun pointsFor2(points: List<Point>, b: Bounds): Sequence<Point> = points.allPairs().flatMap { (x,y)-> candidates2(x, y, b) }


fun to2d(points: List<Point>): List<List<Boolean>> {
    val maxX = points.maxOf { it.first }
    val maxY = points.maxOf { it.second }
    val out = MutableList(maxY+1, { MutableList(maxX+1, {false})})
    for ((x,y) in points) {
        out[y][x] = true
    }
    return out
}
fun <A : Any> toString(ls: List<List<A>>, transform: ((A)-> CharSequence)?=null): String = ls.joinToString("\n") { it.joinToString("", transform=transform) }
fun pretty(points: List<Point>) = toString(to2d(points)) {if (it) { "#" } else { "." }}

fun size(s: String): Bounds {
    val lines = s.lines()
    return Bounds(lines[0].length, lines.size)
}

fun main() {
    val inp = getResourceAsText("Day08.txt")!!
    val bounds = size(inp)
    val part1 = parseGrid(inp).values.asSequence()
//        .forEach {
//                it.allPairs().forEach { (x, y) ->
//                    val diff = x-y
//                    println("$x, $y, $diff, ${gcd(diff.first, diff.second) } ${(x-y).norm()}")
//                    println(Grid<Char>(0, bounds.width, 0, bounds.height, default=' ')
//                        .apply {
//                            paint('#', candidates2(x, y, bounds).toList())
//                            paint('A', points = listOf(x, y))
//                        })
//                }
//        }

        .flatMap{pointsFor2(it, bounds)}
        .distinct()
        .filter(bounds::inBounds)
        .count()
    println(part1)
}