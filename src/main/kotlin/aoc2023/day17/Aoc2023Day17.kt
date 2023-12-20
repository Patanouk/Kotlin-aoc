package aoc2023.day17

import readInput
import kotlin.math.min

object Aoc2023Day17 {

    private val input = readInput("/aoc2023/day17/input.txt")
        .map { it.map { it.digitToInt() } }

    fun solveFirstStar(): Int {
        val startingPosition = Coordinates(0, 0)
        val initialPossibleState = CoordinateWithDirectionAndDistance(startingPosition, Direction.right, 1)
        val initialPossibleState2 = CoordinateWithDirectionAndDistance(startingPosition, Direction.down, 1)

        return shortestPath(mutableSetOf(initialPossibleState, initialPossibleState2), mutableMapOf(initialPossibleState to 0, initialPossibleState2 to 0), mutableSetOf())
    }

    private fun shortestPath(
        nodesToVisit: MutableSet<CoordinateWithDirectionAndDistance>,
        pathCosts: MutableMap<CoordinateWithDirectionAndDistance, Int>,
        visitedNodes: MutableSet<CoordinateWithDirectionAndDistance>,
    ): Int {
        while (true) {
            val currentPos = nodesToVisit.sortedBy { pathCosts[it] }.first()

            if (currentPos.coordinates == Coordinates(input.indices.last, input[0].indices.last)) {
                return pathCosts[currentPos]!!
            }

            val neighbours = getNextNodes(currentPos)

            neighbours.filter { !visitedNodes.contains(it) }
                .forEach {
                    pathCosts[it] = min(pathCosts[currentPos]!! + input[it.coordinates.x][it.coordinates.y], pathCosts.getOrDefault(it, Int.MAX_VALUE))
                    nodesToVisit.add(it)
                }

            visitedNodes.add(currentPos)
            nodesToVisit.remove(currentPos)
        }
    }

    private fun getNextNodes(c: CoordinateWithDirectionAndDistance): Set<CoordinateWithDirectionAndDistance> {
        val result = mutableSetOf<CoordinateWithDirectionAndDistance>()
        val currentPos = c.coordinates

        when (c.direction) {
            Direction.up,
            Direction.down -> {
                result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x, currentPos.y - 1), Direction.left, 1))
                result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x, currentPos.y + 1), Direction.right, 1))
            }
            Direction.left,
            Direction.right -> {
                result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x - 1, currentPos.y), Direction.up, 1))
                result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x + 1, currentPos.y), Direction.down, 1))
            }
        }

        if (c.distance < 3) {
            when (c.direction) {
                Direction.up -> result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x - 1, currentPos.y), Direction.up, c.distance + 1))
                Direction.down -> result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x + 1, currentPos.y), Direction.down, c.distance + 1))
                Direction.left -> result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x, currentPos.y - 1), Direction.left, c.distance + 1))
                Direction.right -> result.add(CoordinateWithDirectionAndDistance(Coordinates(currentPos.x, currentPos.y + 1), Direction.right, c.distance + 1))
            }
        }

        return result
            .filter { input.indices.contains(it.coordinates.x) && input[0].indices.contains(it.coordinates.y) }
            .toSet()
    }

    private data class CoordinateWithDirectionAndDistance(
        val coordinates: Coordinates,
        val direction: Direction,
        val distance: Int,
    )

    private data class Coordinates(
        val x: Int,
        val y: Int,
    )

    private enum class Direction {
        up,down,left,right,
    }
}