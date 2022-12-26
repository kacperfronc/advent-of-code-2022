fun main() {

    data class Move(val count: Int, val origin: Int, val destination: Int)

    fun extractStacks(input: List<String>): List<ArrayDeque<String>> {
        val cratesWithIndexRow = input.takeWhile { it.isNotBlank() }.map { it.chunked(4) }
        val numberOfStacks = cratesWithIndexRow.last().count()
        val crates = cratesWithIndexRow.dropLast(1)
        val result = mutableListOf<ArrayDeque<String>>()
        for (i in 1..numberOfStacks) {
            result.add(ArrayDeque())
        }
        crates.forEach {
            it.forEachIndexed { index, string ->
                if (string.isNotBlank()) {
                    result[index].add(string.trim())
                }
            }
        }
        return result
    }

    fun extractMoves(input: List<String>): List<IntArray> {
        val moves = input.dropWhile { it.isNotBlank() }.drop(1)
        return moves.map { it.split(" ") }.map {
            intArrayOf(it[1].toInt(), it[3].toInt(), it[5].toInt())
        }
    }

    fun move(it: IntArray): Move {
        return Move(it[0], it[1] - 1, it[2] - 1)
    }

    fun part1(input: List<String>): String {
        val stacks = extractStacks(input)
        val moves = extractMoves(input)
        moves.forEach {
            val (count, origin, destination) = move(it)
            for (i in 1..count) {
                val movedElement = stacks[origin].removeFirst()
                stacks[destination].addFirst(movedElement)
            }
        }
        return stacks.joinToString(separator = "") { it.removeFirst()[1] + "" }
    }

    fun part2(input: List<String>): String {
        val stacks = extractStacks(input)
        val moves = extractMoves(input)
        moves.forEach {
            val (count, origin, destination) = move(it)
            val tempDeq = ArrayDeque<String>()
            for (i in 1..count) {
                val movedElement = stacks[origin].removeFirst()
                tempDeq.addFirst(movedElement)
            }
            for (i in 1..count) {
                val movedElement = tempDeq.removeFirst()
                stacks[destination].addFirst(movedElement)
            }
        }
        return stacks.joinToString(separator = "") { it.removeFirst()[1] + "" }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day05_test")
//    val testResult = part2(testInput)
//    println(testResult)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
