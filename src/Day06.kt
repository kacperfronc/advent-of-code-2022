fun main() {

    fun getDistinctIndex(input: String, distinctCount: Int): Int {
        val chars = input.toCharArray()
        for (i in (distinctCount - 1) until chars.size) {
            val distinctList = (0 until distinctCount).map { chars[i - it] }.distinct()
            if (distinctList.count() == distinctCount) {
                return i + 1
            }
        }
        return -1
    }

    fun part1(input: String): Int {
        return getDistinctIndex(input, 4)
    }

    fun part2(input: String): Int {
        return getDistinctIndex(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")[0]
    val testResult = part2(testInput)
    println(testResult)

    val input = readInput("Day06")[0]
    println(part1(input))
    println(part2(input))
}
