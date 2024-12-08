fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

fun <A> Sequence<A>.loops(): Boolean {
    val seen = mutableSetOf<A>()
    forEach {
        if (it in seen) {
            return true
        }
        seen.add(it)
    }
    return false
}