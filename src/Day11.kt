import java.math.BigInteger

fun main() {

    data class Monkey(
        val items: MutableList<BigInteger>,
        val operation: (old: BigInteger) -> BigInteger,
        val nextMonkey: (worryLevel: BigInteger) -> Int,
        var inspectedItemsCount: Long = 0
    )

    fun textToOperation(input: List<String>): (BigInteger) -> BigInteger {
        if (input[0] == "*") {
            if (input[1] == "old") {
                return { it * it }
            }
            return { it * input[1].toBigInteger() }
        } else {
            if (input[1] == "old") {
                return { it + it }
            }
            return { it + input[1].toBigInteger() }
        }
    }

    fun textToTest(input: List<String>): (BigInteger) -> Int {
        val testDivisibleBy = input[0].substringAfter("Test: divisible by ").toBigInteger()
        val trueCondition = input[1].substringAfter("If true: throw to monkey ").toInt()
        val falseCondition = input[2].substringAfter("If false: throw to monkey ").toInt()

        return { if (it.remainder(testDivisibleBy) == BigInteger.ZERO) trueCondition else falseCondition }
    }

    fun parseInput(input: List<String>): Monkey {
        val items = input[1].substringAfter("Starting items: ").split(", ")
            .map { BigInteger.valueOf(it.toLong()) }
            .toMutableList()
        val operationString = input[2].substringAfter("Operation: new = old ").split(" ")
        val operation = textToOperation(operationString)
        val test = textToTest(listOf(input[3], input[4], input[5]))

        return Monkey(items, operation, test)
    }

    fun part1(input: List<String>): Long {
        val monkeys = input.filter { it.isNotBlank() }.chunked(6).map { parseInput(it) }
        for (i in 0..19) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val worryLevelAfterInspection = monkey.operation(item) / BigInteger.valueOf(3)
                    val nextMonkey = monkey.nextMonkey(worryLevelAfterInspection)
                    monkeys[nextMonkey].items.add(worryLevelAfterInspection)
                    monkey.inspectedItemsCount++
                }
                monkey.items.clear()
            }
        }
        return monkeys.map { it.inspectedItemsCount }.sortedDescending().take(2).reduce { acc, e -> acc * e }
    }

    fun part2(input: List<String>): Long {
        // optimization
        val productOfAllTests = input.filter { it.contains("Test: divisible by ") }
            .map { it.substringAfter("Test: divisible by ").toInt() }
            .reduce { acc, e -> acc * e }

        val monkeys = input.filter { it.isNotBlank() }.chunked(6).map { parseInput(it) }
        for (i in 0..9999) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val worryLevelAfterInspection = monkey.operation(item) % productOfAllTests.toBigInteger()
                    val nextMonkey = monkey.nextMonkey(worryLevelAfterInspection)
                    monkeys[nextMonkey].items.add(worryLevelAfterInspection)
                    monkey.inspectedItemsCount++
                }
                monkey.items.clear()
            }
        }

        return monkeys.map { it.inspectedItemsCount }.sortedDescending().take(2).reduce { acc, e -> acc * e }
    }

//     test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day11_test")
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
