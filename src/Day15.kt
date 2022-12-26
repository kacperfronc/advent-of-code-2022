import kotlin.math.absoluteValue

data class Position(val x: Int, val y: Int)

data class Sensor(val position: Position, val closestBeaconPosition: Position)

fun main() {

    fun manhattanDistance(a: Position, b: Position) = (a.x - b.x).absoluteValue + (a.y - b.y).absoluteValue

    fun position(x: List<Int>): Position {
        if (x.size != 2) throw IllegalArgumentException()
        return Position(x[0], x[1])
    }

    fun parseInput(input: List<String>): List<Sensor> {
        return input.map { it.substringAfter("Sensor at ").split(": closest beacon is at ") }
            .map {
                Sensor(
                    position(it[0].split(", ").map { cords -> cords.substringAfter("=").toInt() }),
                    position(it[1].split(", ").map { cords -> cords.substringAfter("=").toInt() })
                )
            }
    }

    fun cantHoldBeacon(sensors: List<Sensor>, i: Int, row: Int) =
        sensors.map { it.closestBeaconPosition }.none { it.x == i && it.y == row } && sensors.any { sensor ->
            manhattanDistance(sensor.position, sensor.closestBeaconPosition) >= manhattanDistance(
                sensor.position,
                Position(i, row)
            )
        }

    fun part1(input: List<String>, row: Int): Int {
        val sensors = parseInput(input)

        val maxDist = sensors.maxOf { manhattanDistance(it.position, it.closestBeaconPosition) }
        val xMin = sensors.minOf { it.position.x - maxDist }
        val xMax = sensors.maxOf { it.position.x + maxDist }
        return IntRange(xMin, xMax).count { i -> cantHoldBeacon(sensors, i, row) }
    }

    fun part2(input: List<String>, max: Int): Long {
        val sensors = parseInput(input)
        var result: Position? = null
        outer@ for (sensor in sensors) {
            val manhattanDistance = manhattanDistance(sensor.position, sensor.closestBeaconPosition) + 1
            for (i in 0..manhattanDistance) {
                if (!cantHoldBeacon(sensors, sensor.position.x + i, sensor.position.y + manhattanDistance - i)) {
                    result = Position(sensor.position.x + i, sensor.position.y + manhattanDistance - i)
                }
                if (!cantHoldBeacon(sensors, sensor.position.x + i, sensor.position.y - manhattanDistance + i)) {
                    result = Position(sensor.position.x + i, sensor.position.y - manhattanDistance + i)
                }
                if (!cantHoldBeacon(sensors, sensor.position.x - i, sensor.position.y - manhattanDistance + i)) {
                    result = Position(sensor.position.x - i, sensor.position.y - manhattanDistance + i)
                }
                if (!cantHoldBeacon(sensors, sensor.position.x - i, sensor.position.y + manhattanDistance - i)) {
                    result = Position(sensor.position.x - i, sensor.position.y + manhattanDistance - i)
                }
                if (result != null) {
                    if (result.x >= 0 && result.y >= 0 && result.x <= max && result.y <= max) {
                        break@outer
                    }
                }
            }
        }
        return result?.let {
            it.x * 4000000L + it.y
        } ?: 0
    }

//     test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day15_test")
//    println(part1(testInput, 10))
//    println(part2(testInput, 20))

    val input = readInput("Day15")
//    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
