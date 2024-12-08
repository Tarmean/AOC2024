fun main() {
    val grid = Grid.parse(getResourceAsText("Day06.txt")!!)
    val traced = trace(grid).map { it.first }.toSet()
    println(traced.size)
    val startPos = startPos(grid).first
    val part2 = traced.asSequence()
        .filter { it != startPos }
        .filter { trace(grid.with(it, '#')).loops() }
        .count()
    println(part2)
}

fun startPos(grid: Grid<Char>): Pair<Point, Char> = grid.findAll { it in "<>v^" }.first()

fun isWall(c: Char?): Boolean = c == '#'

fun toDir(chr: Char): Dir = when (chr) {
    '<' -> Dir.Left
    '>' -> Dir.Right
    '^' -> Dir.Up
    'v' -> Dir.Down
    else -> throw IllegalStateException("Dir $chr is not a direction")
}

fun Grid<Char>.stillFree(pos: Point, dir: Dir): Sequence<Dir> =
    (0..3).asSequence()
        .map { dir.rotate(it) }
        .filter { !isWall(this[pos + it.dir]) }

fun trace(grid: Grid<Char>) = sequence<Pair<Point, Dir>> {
    var (pos, startChar) = startPos(grid)
    var dir = toDir(startChar)
    while (grid.inBounds(pos)) {
        yield(Pair(pos, dir))
        val next = grid.stillFree(pos, dir).firstOrNull()
        if (next != null) {
            pos = pos + next.dir
            dir = next
        } else {
            return@sequence
        }
    }
}