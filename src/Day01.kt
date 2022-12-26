fun main() {
    fun splitInput(input: List<String>): MutableList<MutableList<String>> {
        val output = mutableListOf<MutableList<String>>()
        output.add(mutableListOf())
        input.forEach {
            if (it.isBlank()) {
                output.add(mutableListOf())
            } else {
                val last = output.last()
                last.add(it)
            }
        }
        return output
    }

    fun part1(input: List<String>): Int {
        val output = splitInput(input)

        val result = output.maxBy { it.reduce { f, s -> "" + (f.toInt() + s.toInt()) }.toInt() }
        return result.reduce { f, s -> "" + (f.toInt() + s.toInt()) }.toInt()
    }

    fun part2(input: List<String>): Int {
        val output = splitInput(input)

        val result = output.sortedByDescending { it.reduce { f, s -> "" + (f.toInt() + s.toInt()) }.toInt() }

        val first = result[0].reduce { f, s -> "" + (f.toInt() + s.toInt()) }.toInt()
        val second = result[1].reduce { f, s -> "" + (f.toInt() + s.toInt()) }.toInt()
        val third = result[2].reduce { f, s -> "" + (f.toInt() + s.toInt()) }.toInt()
        return first + second + third
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
