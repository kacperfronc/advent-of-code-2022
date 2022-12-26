import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

fun main() {

    val objectMapper = ObjectMapper()

    fun isRightOrder(left: JsonNode, right: JsonNode): Int {

        left.forEachIndexed { i, leftNode ->
            if (right.size() <= i) {
                return -1
            }
            val rightNode = right[i]
            if (leftNode.isInt && rightNode.isInt) {
                if (leftNode.intValue() < rightNode.intValue()) {
                    return 1
                } else if (leftNode.intValue() > rightNode.intValue()) {
                    return -1
                }
            } else if (leftNode.isInt) {
                val intValue = leftNode.intValue()
                val rightOrder = isRightOrder(objectMapper.createArrayNode().add(intValue), rightNode)
                if (rightOrder != 0) {
                    return rightOrder
                }
            } else if (rightNode.isInt) {
                val intValue = rightNode.intValue()
                val rightOrder = isRightOrder(leftNode, objectMapper.createArrayNode().add(intValue))
                if (rightOrder != 0) {
                    return rightOrder
                }
            }
            val rightOrder = isRightOrder(leftNode, rightNode)
            if (rightOrder != 0) {
                return rightOrder
            }
        }
        if (left.size() < right.size()) {
            return 1
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        val chunked = input.filter { it.isNotBlank() }.chunked(2)
            .map { Pair(objectMapper.readTree(it[0]), objectMapper.readTree(it[1])) }

        return chunked.mapIndexed { index, pair ->
            if (isRightOrder(pair.first, pair.second) == 1) index + 1 else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        fun wrapValueInTree(value: Int): ArrayNode {
            return objectMapper.createArrayNode().add(
                objectMapper.createArrayNode().add(
                    objectMapper.createArrayNode().add(value)
                )
            )
        }

        val chunked = input.filter { it.isNotBlank() }.map { objectMapper.readTree(it) }
        val firstDistress = wrapValueInTree(2)
        val secondDistress = wrapValueInTree(6)
        val listWithAdditionalSignals = chunked + firstDistress + secondDistress

        fun isDistress(value: JsonNode): Boolean {
            if (value.isArray && value.size() == 1) {
                val nested = value[0]
                if (nested.isArray && nested.size() == 1) {
                    val doubleNested = nested[0]
                    if (doubleNested.isInt && doubleNested.size() == 0) {
                        val intValue = doubleNested.intValue()
                        return intValue == 2 || intValue == 6
                    }
                }
            }
            return false
        }

        return listWithAdditionalSignals.sortedWith { p0, pi -> isRightOrder(pi, p0) }
            .mapIndexed { index, jsonNode ->
                if (isDistress(jsonNode)) index + 1 else 1
            }.reduce { acc, e -> acc * e }
    }

//     test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day13_test")
//    println(part1(testInput))
//    println(part2(testInput))

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
