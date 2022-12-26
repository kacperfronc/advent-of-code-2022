import kotlin.math.absoluteValue

enum class Command {
    NOOP, ADD_X
}

fun main() {
    data class Operation(val command: Command, val argument: Int? = null)

    fun parseOperations(input: List<String>): List<Operation> {
        return input.map {
            if (it == "noop") {
                Operation(Command.NOOP)
            } else {
                Operation(Command.ADD_X, it.split(" ")[1].toInt())
            }
        }
    }

    fun part1(input: List<String>): Int {
        val operations = parseOperations(input)
        var xRegistry = 1
        var cycle = 1
        var signalStrengthSum = 0

        fun isStrengthMeasuringCycle(cycle: Int): Boolean {
            return (cycle + 20) % 40 == 0
        }

        operations.forEach {
            if (isStrengthMeasuringCycle(cycle)) {
                signalStrengthSum += (cycle) * xRegistry
            }
            cycle++
            if (it.command == Command.ADD_X) {
                if (isStrengthMeasuringCycle(cycle)) {
                    signalStrengthSum += (cycle) * xRegistry
                }
                cycle++
                xRegistry += (it.argument ?: 0)
            }

        }
        return signalStrengthSum
    }

    fun part2(input: List<String>): Int {

        fun drawPixel(cycle: Int, xRegistry: Int, result: Array<CharArray>) {
            val row = (cycle) / 40
            val column = xRegistry - 1
            val crtPosition = (cycle % 40) - 1
            if ((xRegistry - crtPosition).absoluteValue <= 1) {
                if (row in 0..5 && column in 0..39) {
                    result[row][crtPosition] = '#'
                }
            }
        }

        val operations = parseOperations(input)
        var xRegistry = 1
        var cycle = 1
        val result = Array(size = 6, init = { CharArray(40) { '.' } })

        for (it in operations) {
            drawPixel(cycle++, xRegistry, result)
            if (it.command == Command.ADD_X) {
                drawPixel(cycle++, xRegistry, result)
                xRegistry += (it.argument ?: 0)
            }
        }
        result.forEach { println(it) }
        return -1
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
