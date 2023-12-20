package aoc2023.day17

import readInput
import kotlin.math.min

object Aoc2023Day17 {

    private val input = readInput("/aoc2023/day17/input.txt")
        .map { it.map { it.digitToInt() } }

    fun solveFirstStar(): Int {
        val startingPosition = Coordinates(0, 0)
        return shortestPath(mutableSetOf(startingPosition), mutableMapOf(startingPosition to 0), mutableSetOf())
    }

    private fun shortestPath(
        nodesToVisit: MutableSet<Coordinates>,
        pathCosts: MutableMap<Coordinates, Int>,
        visitedNodes: MutableSet<Coordinates>,
    ): Int {
        while (true) {
            val currentPos = nodesToVisit.sortedBy { pathCosts[it] }.first()

            if (currentPos == Coordinates(input.indices.last, input[0].indices.last)) {
                return pathCosts[currentPos]!!
            }

            val neighbours = getNeighbours(currentPos)

            neighbours.filter { !visitedNodes.contains(it) }
                .forEach {
                    pathCosts[it] = min(pathCosts[currentPos]!! + input[it.x][it.y], pathCosts.getOrDefault(it, Int.MAX_VALUE))
                    nodesToVisit.add(it)
                }

            visitedNodes.add(currentPos)
            nodesToVisit.remove(currentPos)
        }
    }

    private fun getNeighbours(currentPos: Coordinates): Set<Coordinates> {
        return setOf(
            Coordinates(currentPos.x + 1, currentPos.y),
            Coordinates(currentPos.x - 1, currentPos.y),
            Coordinates(currentPos.x, currentPos.y + 1),
            Coordinates(currentPos.x, currentPos.y - 1),
        )
            .filter { input.indices.contains(it.x) && input[0].indices.contains(it.y) }
            .toSet()
    }

    private data class Coordinates(
        val x: Int,
        val y: Int,
    )
}