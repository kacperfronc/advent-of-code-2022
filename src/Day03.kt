fun main() {
    val scores = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    fun letterScore(input: Char): Int {
        return scores.indexOf(input) + 1
    }

    fun part1(input: List<String>): Int {

        fun rucksackScore(input: String): Int {
            val halvedLength = input.length / 2
            val firstHalf = input.subSequence(0, halvedLength)
            val secondHalf = input.subSequence(halvedLength, input.length)
            val repeats = firstHalf.first { secondHalf.contains(it) }
            return letterScore(repeats)
        }

        return input.sumOf { rucksackScore(it) }
    }

    fun part2(input: List<String>): Int {
        val chunked = input.chunked(3)
        return chunked.sumOf { letterScore(it[0].first { inner -> it[1].contains(inner) && it[2].contains(inner) }) }
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
