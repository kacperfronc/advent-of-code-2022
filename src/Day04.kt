fun main() {

    fun part1(input: List<String>): Int {
        fun overlaps(input: String): Boolean {
            val pairs = input.split(",")
            val firstLowerBound = pairs[0].split("-")[0].toInt()
            val firstUpperBound = pairs[0].split("-")[1].toInt()
            val secondLowerBound = pairs[1].split("-")[0].toInt()
            val secondUpperBound = pairs[1].split("-")[1].toInt()

            if (firstLowerBound <= secondLowerBound && firstUpperBound >= secondUpperBound) {
                return true;
            } else if (secondLowerBound <= firstLowerBound && secondUpperBound >= firstUpperBound) {
                return true;
            }

            return false;
        }
        return input.count { overlaps(it) }
    }

    fun part2(input: List<String>): Int {
        fun overlaps(input: String): Boolean {
            val pairs = input.split(",")
            val firstLowerBound = pairs[0].split("-")[0].toInt()
            val firstUpperBound = pairs[0].split("-")[1].toInt()
            val secondLowerBound = pairs[1].split("-")[0].toInt()
            val secondUpperBound = pairs[1].split("-")[1].toInt()

            if (firstUpperBound < secondLowerBound || secondUpperBound < firstLowerBound) {
                return false;
            }

            return true;
        }
        return input.count { overlaps(it) }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day04_test")
//    val testResult = part2(testInput)
//    println(testResult)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
