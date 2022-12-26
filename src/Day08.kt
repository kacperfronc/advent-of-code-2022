fun main() {

    fun createGrid(input: List<String>): Array<CharArray> {
        val result = Array(size = input.size) { CharArray(input.size) }
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, char ->
                result[rowIndex][columnIndex] = char
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {

        fun isVisible(grid: Array<CharArray>, row: Int, column: Int): Boolean {
            if (row == 0 || column == 0 || row == grid.size - 1 || column == grid.size - 1) {
                return true
            }
            val treeHeight = grid[row][column]
            return (0 until row).all { grid[it][column] < treeHeight }
                .or(((row + 1) until grid.size).all { grid[it][column] < treeHeight })
                .or((0 until column).all { grid[row][it] < treeHeight })
                .or(((column + 1) until grid.size).all { grid[row][it] < treeHeight })
        }

        val grid = createGrid(input)
        return grid.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, _ -> if (isVisible(grid, rowIndex, columnIndex)) 1 else 0 }
        }.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {

        fun scenicScore(grid: Array<CharArray>, row: Int, column: Int): Int {
            if (row == 0 || column == 0 || row == grid.size - 1 || column == grid.size - 1) {
                return 0
            }
            val treeHeight = grid[row][column]

            var scoreUp = 0
            var scoreDown = 0
            var scoreLeft = 0
            var scoreRight = 0

            for (i in (row - 1) downTo 0) {
                scoreUp += 1
                if (grid[i][column] >= treeHeight) {
                    break
                }
            }
            for (i in (row + 1) until grid.size) {
                scoreDown += 1
                if (grid[i][column] >= treeHeight) {
                    break
                }
            }
            for (i in (column - 1) downTo 0) {
                scoreLeft += 1
                if (grid[row][i] >= treeHeight) {
                    break
                }
            }
            for (i in (column + 1) until grid.size) {
                scoreRight += 1
                if (grid[row][i] >= treeHeight) {
                    break
                }
            }

            return scoreUp * scoreDown * scoreLeft * scoreRight
        }

        val grid = createGrid(input)

        return grid.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, _ -> scenicScore(grid, rowIndex, columnIndex) }
        }.maxOf { it.max() }

    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day08_test")
//    val testResult = part2(testInput)
//    println(testResult)

    val input = readInput("Day08")
//    println(part1(input))
    println(part2(input))
}
