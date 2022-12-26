const val startingPosition = 'S'
const val goal = 'E'

fun ensureHeightChar(param: Char): Char {
    return when (param) {
        startingPosition -> 'a'
        goal -> 'z'
        else -> param
    }
}

fun isStepAllowed(
    grid: Array<CharArray>, currentRow: Int, currentColumn: Int, row: Int, column: Int, maxRow: Int, maxColumn: Int
): Boolean {
    if (row < 0 || column < 0) {
        return false
    }
    if (row >= maxRow || column >= maxColumn) {
        return false
    }
    return ensureHeightChar(grid[row][column]) - ensureHeightChar(grid[currentRow][currentColumn]) >= -1
}

fun parseInput(input: List<String>): Array<CharArray> {
    return input.map { it.toCharArray() }.toTypedArray()
}

data class CurrentPath(val rowColumn: Short, val previous: CurrentPath? = null)

fun countPathLength(param: CurrentPath): Int {
    var i = 0
    var curr: CurrentPath? = param
    while (curr != null) {
        i++
        curr = curr.previous
    }
    return i
}

fun enqueue(
    dequeued: CurrentPath,
    row: Int,
    column: Int,
    grid: Array<CharArray>,
    currentRow: Int,
    currentColumn: Int,
    deque: ArrayDeque<CurrentPath>,
    reached: MutableList<Short>
) {
    if (isStepAllowed(grid, currentRow, currentColumn, row, column, grid.size, grid[0].size)) {
        val next = (row * 100 + column).toShort()
        if (!reached.contains(next)) {
            deque.add(CurrentPath(next, dequeued))
        }
    }
}

fun part1(input: List<String>): Int {
    val grid = parseInput(input)
    val startingCoordinates =
        grid.mapIndexed { index, chars -> Pair(index, chars) }.filter { (_, chars) -> chars.contains('E') }
            .map { (index, chars) -> Pair(index, chars.indexOf('E')) }.first()
    val deque = ArrayDeque<CurrentPath>()
    deque.add(CurrentPath((startingCoordinates.first * 100 + startingCoordinates.second).toShort()))
    val reached = mutableListOf<Short>()
    val results = mutableListOf<CurrentPath>()

    while (deque.isNotEmpty()) {
        val dequeued = deque.removeFirst()
        if (reached.contains(dequeued.rowColumn)) {
            continue
        }
        val row = dequeued.rowColumn / 100
        val column = dequeued.rowColumn % 100

        if (grid[row][column] == 'S') {
            results += dequeued
        }
        reached += dequeued.rowColumn

        enqueue(dequeued, row - 1, column, grid, row, column, deque, reached)
        enqueue(dequeued, row + 1, column, grid, row, column, deque, reached)
        enqueue(dequeued, row, column - 1, grid, row, column, deque, reached)
        enqueue(dequeued, row, column + 1, grid, row, column, deque, reached)
    }
    return (results.minOfOrNull { countPathLength(it) - 1 } ?: 0)
}

fun part2(input: List<String>): Int {
    val grid = parseInput(input)
    val startingCoordinates =
        grid.mapIndexed { index, chars -> Pair(index, chars) }.filter { (_, chars) -> chars.contains('E') }
            .map { (index, chars) -> Pair(index, chars.indexOf('E')) }.first()
    val deque = ArrayDeque<CurrentPath>()
    deque.add(CurrentPath((startingCoordinates.first * 100 + startingCoordinates.second).toShort()))
    val reached = mutableListOf<Short>()
    val results = mutableListOf<CurrentPath>()

    while (deque.isNotEmpty()) {
        val dequeued = deque.removeFirst()
        if (reached.contains(dequeued.rowColumn)) {
            continue
        }
        val row = dequeued.rowColumn / 100
        val column = dequeued.rowColumn % 100

        if (ensureHeightChar(grid[row][column]) == 'a') {
            results += dequeued
        }
        reached += dequeued.rowColumn

        enqueue(dequeued, row - 1, column, grid, row, column, deque, reached)
        enqueue(dequeued, row + 1, column, grid, row, column, deque, reached)
        enqueue(dequeued, row, column - 1, grid, row, column, deque, reached)
        enqueue(dequeued, row, column + 1, grid, row, column, deque, reached)
    }
    return (results.minOfOrNull { countPathLength(it) - 1 } ?: 0)
}

fun main() {


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
