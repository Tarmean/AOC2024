class Grid<A> {
    private var content: MutableList<MutableList<A?>>
    val x0: Int;
    val x1: Int;
    val y0: Int;
    val y1: Int;
    constructor(x0: Int, x1: Int, y0: Int, y1: Int,default: A?=null) {
        this.x0=x0
        this.x1=x1
        this.y0=y0
        this.y1=y1
        content = MutableList(y1-y0+1, { MutableList(x1-x0+1, {default})})
    }
    private constructor(x0: Int, x1: Int, y0: Int, y1: Int,body: MutableList<MutableList<A?>>) {
        this.x0=x0
        this.x1=x1
        this.y0=y0
        this.y1=y1
        content = body
    }

    fun clone(): Grid<A> = Grid(x0,x1,y0,y1,content.map {it.toMutableList() }.toMutableList())

    fun inBounds(point: Point) = point.first in x0..x1 && point.second in y0..y1

    fun findAll(predicate: (A)-> Boolean): Sequence<Pair<Point, A>> = sequence {
        for ((y, ln) in content.withIndex()) {
            for ((x, c) in ln.withIndex()) {
                if (c != null && predicate(c)) {
                    yield(Pair(Pair(x,y), c))
                }
            }
        }
    }


    fun paint(chr: A, points: Iterable<Point>, merge: ((Point, A, A)->A)? = null) {
        for ((x,y) in points) {
            if (merge != null && content[y][x] != null) {
                content[y][x] = merge(Pair(x,y), content[y][x]!!, chr)
            } else {
                content[y][x] = chr
            }
        }
    }

    operator fun get(pair: Pair<Int, Int>): A? = pair.takeIf( this::inBounds )?.let{ (x,y)-> content[y][x] }
    fun toStringT(transform: ((A?)->CharSequence)? = null): String = content.joinToString("\n") { it.joinToString("", transform=transform) }
    override fun toString(): String = toStringT(null)

    fun with(pos: Pair<Int, Int>, value: A): Grid<A> = clone().also { it.content[pos.second][pos.first] = value}

    companion object {
        fun <A> parse(input: String, transform: (Int, Int, Char) -> A?): Grid<A> {
            val lines = input.lines()
            val height = lines.size
            val width = lines[0].length
            check(height > 0)
            check(width > 0)

            val body = MutableList(height, { MutableList<A?>(width, { null }) })
            for ((y, ln) in lines.withIndex()) {
                for ((x,c) in ln.withIndex()) {
                    body[y][x] = transform(x,y,c)
                }
            }
            return Grid(0, width-1, 0, height-1, body)
        }
        fun parse(input: String, empty: Char?=null): Grid<Char> = parse(input) {x,y,c -> c.takeIf { it != empty} }
    }

}

enum class Dir(val dir: Point) {
    Left(Point(-1, 0)),
    Up(Point(0, -1)),
    Right(Point(1, 0)),
    Down(Point(0, 1));

    fun rotate(offset: Int): Dir = entries[(ordinal + offset).mod(entries.size)]
}