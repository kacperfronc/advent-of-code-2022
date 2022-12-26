import kotlin.math.absoluteValue


fun main() {
    val startingPoint = Pair(0, 0)

    fun isDetachedFromPrevious(previousElement: Pair<Int, Int>, element: Pair<Int, Int>): Boolean {
        return when {
            (previousElement.first - element.first).absoluteValue >= 2 -> true
            (previousElement.second - element.second).absoluteValue >= 2 -> true
            else -> false
        }
    }

    fun moveHead(currentPosition: Pair<Int, Int>, direction: String): Pair<Int, Int> {
        return when (direction) {
            "R" -> Pair(currentPosition.first + 1, currentPosition.second)
            "L" -> Pair(currentPosition.first - 1, currentPosition.second)
            "U" -> Pair(currentPosition.first, currentPosition.second + 1)
            "D" -> Pair(currentPosition.first, currentPosition.second - 1)
            else -> throw IllegalArgumentException(direction)
        }
    }

    fun moveHorizontally(xDifference: Int, currentPosition: Pair<Int, Int>) =
        if (xDifference > 0) {
            Pair(currentPosition.first + 1, currentPosition.second)
        } else {
            Pair(currentPosition.first - 1, currentPosition.second)
        }

    fun moveVertically(yDifference: Int, currentPosition: Pair<Int, Int>) =
        if (yDifference > 0) {
            Pair(currentPosition.first, currentPosition.second + 1)
        } else {
            Pair(currentPosition.first, currentPosition.second - 1)
        }

    fun moveTail(currentPosition: Pair<Int, Int>, headPosition: Pair<Int, Int>): Pair<Int, Int> {
        val xDifference = headPosition.first - currentPosition.first
        val yDifference = headPosition.second - currentPosition.second
        return if (yDifference == 0) {
            moveHorizontally(xDifference, currentPosition)
        } else if (xDifference == 0) {
            moveVertically(yDifference, currentPosition)
        } else {
            moveVertically(yDifference, moveHorizontally(xDifference, currentPosition))
        }
    }

    fun part1(input: List<String>): Int {

        val tailVisitedPositions = mutableListOf<Pair<Int, Int>>()
        var headPosition = startingPoint
        var tailPosition = startingPoint
        tailVisitedPositions.add(tailPosition)

        input.forEach {
            val split = it.split(" ")
            val direction = split[0]
            val steps = split[1].toInt()
            for (i in 1..steps) {
                headPosition = moveHead(headPosition, direction)
                // if tail far enough move too
                if (isDetachedFromPrevious(headPosition, tailPosition)) {
                    tailPosition = moveTail(tailPosition, headPosition)
                    tailVisitedPositions.add(tailPosition)
                }
            }
        }

        return tailVisitedPositions.distinct().count()
    }

    data class Position(var current: Pair<Int, Int>, val previous: MutableList<Pair<Int, Int>>)

    fun part2(input: List<String>): Int {

        var headPosition = startingPoint
        val tailPosition = Array(9) { Position(startingPoint, mutableListOf()) }

        input.forEach {
            val split = it.split(" ")
            val direction = split[0]
            val steps = split[1].toInt()
            for (i in 1..steps) {
                headPosition = moveHead(headPosition, direction)
                // if tail far enough move too
                if (isDetachedFromPrevious(headPosition, tailPosition[0].current)) {
                    val current = tailPosition[0].current
                    tailPosition[0].previous += Pair(current.first, current.second)
                    tailPosition[0].current = moveTail(tailPosition[0].current, headPosition)
                }
                for (x in 1 until tailPosition.size) {
                    if (isDetachedFromPrevious(tailPosition[x - 1].current, tailPosition[x].current)) {
                        val previousKnotPosition = tailPosition[x - 1].current
                        val nextCurrentKnotPosition = moveTail(tailPosition[x].current, previousKnotPosition)
                        tailPosition[x].current = nextCurrentKnotPosition
                        tailPosition[x].previous += Pair(nextCurrentKnotPosition.first, nextCurrentKnotPosition.second)
                    }
                }
            }
        }

        val last = tailPosition.last()
        last.previous += last.current
        return last.previous.distinct().count() + 1
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    println(part1(testInput))
    val testInput2 = readInput("Day09_test2")
    println(part2(testInput2))

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
