package day8

import readInput
import java.lang.IllegalArgumentException

object Aoc2023Day8 {


    fun solveFirstStar(): Int {
        val input = readInput("/day8/input.txt")
        val directions = input.first()

        val nodes = input.subList(2, input.size)
            .map { it.split(" = ") }.associate { Pair(it[0], Node.build(it[1])) }

        var currentNode = "AAA"
        var numberOfSteps = 0

        while (currentNode != "ZZZ") {
            currentNode = getNextNode(currentNode, nodes, directions, numberOfSteps)
            numberOfSteps++
        }

        return numberOfSteps
    }

    fun solveSecondStar(): Int {
        val input = readInput("/day8/input.txt")
        val directions = input.first()

        val nodes = input.subList(2, input.size)
            .map { it.split(" = ") }.associate { Pair(it[0], Node.build(it[1])) }

        var currentNodes = nodes.keys.filter { it.endsWith('A') }
        var numberOfSteps = 0

        while (!currentNodes.all { it.endsWith('Z') }) {
            currentNodes = currentNodes.map { getNextNode(it, nodes, directions, numberOfSteps) }
            numberOfSteps++
            println(numberOfSteps)
        }

        return numberOfSteps
    }

    private fun getNextNode(currentNode: String, nodes: Map<String, Node>, directions: String, numberOfSteps: Int) =
        when (val direction = directions[numberOfSteps % directions.length]) {
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