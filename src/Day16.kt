data class Valve(val name: String, val flowRate: Int, val connections: List<String>)
data class Scenario(val position: Valve, val actions: List<Action>)

sealed class Action

data class Move(val from: Valve, val to: Valve) : Action()
data class Open(val valve: Valve) : Action()
data class Wait(val valve: Valve) : Action()

fun main() {

    fun parseInput(input: List<String>): List<Valve> {
        fun parseSingle(input: String): Valve {
            val cleaned =
                input.removePrefix("Valve ").replace("has flow rate=", "").replace("tunnels lead to valves ", "")
                    .replace("tunnel leads to valve ", "").split("; ")
            val valveDesc = cleaned[0].split(" ")
            return Valve(valveDesc[0], valveDesc[1].toInt(), cleaned[1].split(", "))
        }
        return input.map { parseSingle(it) }
    }

    fun distance(from: Valve, to: Valve, valves: List<Valve>): Int {
        val route = ArrayDeque<List<Valve>>()
        route.add(listOf(from))
        while (true) {
            val currentPath = route.removeFirst()
            val last = currentPath.last()
            if (last == to) {
                return currentPath.size - 1
            }
            last.connections.forEach {
                val valve = valves.find { valve -> valve.name == it }!!
                if (!currentPath.contains(valve)) {
                    route.add(currentPath + valve)
                }
            }
        }

    }

    fun isOpenedValve(valve: Valve, actions: List<Action>): Boolean {
        return actions.any { it is Open && it.valve == valve }
    }

    fun distanceToClosestNotOpened(valve: Valve, valves: List<Valve>, actions: List<Action>): Int {
        if (valve.flowRate > 0 && !isOpenedValve(valve, actions)) {
            return 0
        }

        return valves.filter { it.flowRate > 0 && !isOpenedValve(it, actions) }
            .minOfOrNull { distance(valve, it, valves) } ?: Int.MAX_VALUE
    }

    fun isCirculatingWithoutOpening(to: Valve, actions: List<Action>): Boolean {
        val indexOfLast = actions.indexOfLast { it is Open }
        if (indexOfLast == actions.size - 1) {
            return false
        }
        return actions.drop(indexOfLast.coerceAtLeast(0)).filterIsInstance<Move>().any { to == it.from }
    }

    fun countScore(scenario: Scenario): Int {
        var currentRelease = 0
        for (i in 1 until scenario.actions.size) {
            val subList = scenario.actions.subList(0, i)
            subList.filterIsInstance<Open>().forEach {
                currentRelease += it.valve.flowRate
            }
        }
        return currentRelease
    }

    fun part1(input: List<String>): Int {
        val valves = parseInput(input)
        val startingValve = valves[0]
        val scenarios = ArrayDeque<Scenario>()
        var currentSize = 0
        if (startingValve.flowRate > 0) {
            scenarios.add(Scenario(startingValve, listOf(Open(startingValve))))
        }
        startingValve.connections.forEach {
            val valve = valves.find { valve -> valve.name == it }!!
            scenarios.add(Scenario(valve, listOf(Move(startingValve, valve))))
        }
        while (scenarios.any { it.actions.size < 30 }) {
            val (position, actions) = scenarios.removeFirst()
            if (actions.size < 30) {
                if (position.flowRate > 0 && !isOpenedValve(position, actions)) {
                    scenarios.add(Scenario(position, actions + Open(position)))
                }
                val remainingActions = 30 - actions.size
                if (valves.count { it.flowRate > 0 } == actions.filterIsInstance<Open>().size ||
                    distanceToClosestNotOpened(position, valves, actions) > 30 - actions.size) {
                    val size = remainingActions - 1
                    scenarios.add(Scenario(position, actions + 0.rangeTo(size).map { Wait(position) }))
                }
                position.connections.forEach {
                    val valve = valves.find { valve -> valve.name == it }!!
                    val last = actions.last()
                    if (last is Open || last is Move && last.from != valve) {
                        if (!isCirculatingWithoutOpening(valve, actions)) {
                            scenarios.add(Scenario(valve, actions + Move(position, valve)))
                        }
                    }
                }
            }
            if (actions.size > currentSize) {
                currentSize = actions.size
                println("actions $actions")
                println("actions size: $currentSize")
            }
        }

        return scenarios.maxOf { countScore(it) }
    }

    fun part2(input: List<String>): Int {
        return -1
    }

//     test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day16_test")
//    println(part1(testInput))
//    println(part2(testInput))

    //calcualte each round score and if same valves are opened just drop them
    val input = readInput("Day16")
    println(part1(input))
//    println(part2(input))
}
