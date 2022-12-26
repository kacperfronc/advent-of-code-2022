enum class Field {
    AIR, STONE, SAND, SAND_SOURCE;
}

fun main() {

    fun printGrid(grid: Array<Array<Field>>) {
        grid.forEach { row ->
            row.forEach { column ->
                when (column) {
                    Field.SAND -> print("o")
                    Field.STONE -> print("#")
                    Field.SAND_SOURCE -> print("+")
                    else -> print(".")
                }
            }
            println()
        }
    }

    fun parseIntoGrid(input: List<String>, infWidth: Boolean = false): Array<Array<Field>> {
        val stonePaths = input.map { it.split(" -> ") }
        val flatten = stonePaths.flatten().map { it.split(",") }
        val leftmost = if (infWidth) 0 else flatten.minOfOrNull { it[0].toInt() } ?: Int.MAX_VALUE
        val rightmost = if (infWidth) 1000 else flatten.maxOfOrNull { it[0].toInt() } ?: Int.MIN_VALUE
        val depth = flatten.maxOfOrNull { it[1].toInt() } ?: Int.MIN_VALUE

        fun rangeBetween(a: Int, b: Int) = a.coerceAtMost(b)..a.coerceAtLeast(b)
        val grid = Array(depth + 1) { Array(rightmost - leftmost + 1) { Field.AIR } }
        grid[0][500 - leftmost] = Field.SAND_SOURCE
        stonePaths.forEach {
            for (i in 1 until it.size) {
                val first = it[i - 1].split(",")
                val firstColumn = first[0].toInt() - leftmost
                val firstRow = first[1].toInt()
                val second = it[i].split(",")
                val secondColumn = second[0].toInt() - leftmost
                val secondRow = second[1].toInt()
                for (row in rangeBetween(firstRow, secondRow)) {
                    for (column in rangeBetween(firstColumn, secondColumn)) {
                        grid[row][column] = Field.STONE
                    }
                }
            }
        }
        return grid
    }

    fun part1(input: List<String>): Int {
        val grid = parseIntoGrid(input)
        var currentRow = 0
        var currentColumn = grid[0].indexOf(Field.SAND_SOURCE)
        while (true) {
            if (currentColumn < 0 || currentColumn >= grid[0].size || currentRow + 1 >= grid.size) {
                break
            }
            if (grid[currentRow + 1][currentColumn] == Field.AIR) {
                currentRow += 1
            } else {
                if (currentColumn - 1 < 0) {
                    break
                }
                val toLeft = grid[currentRow + 1][currentColumn - 1]
                if (toLeft == Field.AIR) {
                    currentRow += 1
                    currentColumn -= 1
                } else {
                    if (currentColumn + 1 >= grid[0].size) {
                        break
                    }
                    val toRight = grid[currentRow + 1][currentColumn + 1]
                    if (toRight == Field.AIR) {
                        currentRow += 1
                        currentColumn += 1
                    } else {
                        grid[currentRow][currentColumn] = Field.SAND
                        currentRow = 1
                        currentColumn = grid[0].indexOf(Field.SAND_SOURCE)
                    }
                }
            }
        }
        printGrid(grid)
        return grid.sumOf { it.count { it == Field.SAND } }
    }

    fun part2(input: List<String>): Int {
        val initialGrid = parseIntoGrid(input, true)
        val grid = initialGrid + Array(initialGrid[0].size) { Field.AIR } + Array(initialGrid[0].size) { Field.STONE }
        var currentRow = 0
        var currentColumn = grid[0].indexOf(Field.SAND_SOURCE)
        while (true) {
            if (currentColumn < 0 || currentColumn >= grid[0].size || currentRow + 1 >= grid.size) {
                break
            }
            if (grid[currentRow + 1][currentColumn] == Field.AIR) {
                currentRow += 1
            } else {
                if (currentColumn - 1 < 0) {
                    break
                }
                val toLeft = grid[currentRow + 1][currentColumn - 1]
                if (toLeft == Field.AIR) {
                    currentRow += 1
                    currentColumn -= 1
                } else {
                    if (currentColumn + 1 >= grid[0].size) {
                        break
                    }
                    val toRight = grid[currentRow + 1][currentColumn + 1]
                    if (toRight == Field.AIR) {
                        currentRow += 1
                        currentColumn += 1
                    } else {
                        if (grid[currentRow][currentColumn] == Field.SAND_SOURCE) {
                            break
                        }
                        grid[currentRow][currentColumn] = Field.SAND
                        currentRow = 0
                        currentColumn = grid[0].indexOf(Field.SAND_SOURCE)
                    }
                }
            }
        }
        printGrid(grid)
        return grid.sumOf { it.count { it == Field.SAND } } + 1
    }

//     test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day14_test")
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day14")
//    println(part1(input))
    println(part2(input))
}
