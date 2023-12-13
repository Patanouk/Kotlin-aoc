package aoc2023.day8

import readInput
import java.lang.IllegalArgumentException

object Aoc2023Day8 {


    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day8/input.txt")
        val directions = input.first()

        val nodes = input.subList(2, input.size)
            .map { it.split(" = ") }.associate { Pair(it[0], Node.build(it[1])) }

        var currentNode = "AAA"
        var numberOfSteps = 0

        while (currentNode != "ZZZ") {
            currentNode = getNextNode(currentNode, nodes, directions[numberOfSteps % directions.length])
            numberOfSteps++
        }

        return numberOfSteps
    }

    fun solveSecondStar(): Long {
        val input = readInput("/aoc2023/day8/input.txt")
        val directions = input.first()

        val nodes = input.subList(2, input.size)
            .map { it.split(" = ") }.associate { Pair(it[0], Node.build(it[1])) }

        val startingNodes = nodes.keys.filter { it.endsWith('A') }
        val minStepsToZNodesPerNode = mutableMapOf<String, Set<Int>>()

        for (startingNode in startingNodes) {
            val visitedNodes = mutableSetOf<Pair<String, Int>>()
            val minStepsToZNodes = mutableSetOf<Int>()

            var numberOfSteps = 0
            var nextDirection = directions[0]
            var nextNode = getNextNode(startingNode, nodes, nextDirection)
            while (!visitedNodes.contains(Pair(nextNode, numberOfSteps % directions.length))) {
                visitedNodes.add(Pair(nextNode, numberOfSteps % directions.length))

                if (nextNode.endsWith("Z")) {
                    minStepsToZNodes.add(numberOfSteps + 1)
                }

                numberOfSteps++
                nextDirection = directions[numberOfSteps % directions.length]
                nextNode = getNextNode(nextNode, nodes, nextDirection)
            }

            minStepsToZNodesPerNode[startingNode] = minStepsToZNodes
        }

        if (minStepsToZNodesPerNode.values.all { it.size == 1 }) {
         return minStepsToZNodesPerNode.values
             .map { it.first().toLong() }
             .let { findLCMOfListOfNumbers(it) }
        }

        throw RuntimeException("Not solved for the general case")
    }

    private fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1..<numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    private fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    private fun getNextNode(currentNode: String, nodes: Map<String, Node>, direction: Char) =
        when (direction) {
            'L' -> nodes[currentNode]!!.left
            'R' -> nodes[currentNode]!!.right
            else -> throw IllegalArgumentException("Does not support direction $direction")
        }

    private data class Node(
        val left: String,
        val right: String,
    ) {
        companion object {

            fun build(node: String) =
                node.split(", ")
                    .let { Node(it[0].replace("(", ""), it[1].replace(")", "")) }

        }
    }
}