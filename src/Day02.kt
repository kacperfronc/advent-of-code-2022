fun main() {

    fun part1(input: List<String>): Int {

        fun roundScore(opponentChoice: Char, ownChoice: Char): Int {
            when (opponentChoice) {
                'A' -> {
                    return when (ownChoice) {
                        'X' -> 3
                        'Y' -> 6
                        else -> 0
                    }
                }

                'B' -> {
                    return when (ownChoice) {
                        'X' -> 0
                        'Y' -> 3
                        else -> 6
                    }
                }

                else -> {
                    return when (ownChoice) {
                        'X' -> 6
                        'Y' -> 0
                        else -> 3
                    }
                }
            }
        }

        fun choiceScore(choice: Char): Int {
            return when (choice) {
                'X' -> 1
                'Y' -> 2
                'Z' -> 3
                else -> throw RuntimeException("Illegal choice: $choice")
            }
        }

        return input.sumOf { choiceScore(it[2]) + roundScore(it[0], it[2]) }
    }

    fun part2(input: List<String>): Int {
        fun roundScore(ownChoice: Char): Int {
            return when (ownChoice) {
                'X' -> 0
                'Y' -> 3
                else -> 6
            }
        }

        fun choiceScore(opponentChoice: Char, roundOutcome: Char): Int {
            when (opponentChoice) {
                'A' -> {
                    return when (roundOutcome) {
                        'X' -> 3
                        'Y' -> 1
                        else -> 2
                    }
                }

                'B' -> {
                    return when (roundOutcome) {
                        'X' -> 1
                        'Y' -> 2
                        else -> 3
                    }
                }

                else -> {
                    return when (roundOutcome) {
                        'X' -> 2
                        'Y' -> 3
                        else -> 1
                    }
                }
            }

        }

        return input.sumOf { choiceScore(it[0], it[2]) + roundScore(it[2]) }
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
