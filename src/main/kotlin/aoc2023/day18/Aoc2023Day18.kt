package aoc2023.day18

import readInput
import kotlin.math.abs

object Aoc2023Day18 {

    private val input = readInput("/aoc2023/day18/input.txt")
        .map { it.split(' ') }
        .map { DigInstruction(Direction.valueOf(it[0]), it[1].toInt(), it[2].removeSurrounding("(", ")")) }


    fun solveFirstStar(): Int {
        var currentPosition = Coordinates(0, 0)
        val visitedCoordinates = mutableListOf(currentPosition)

        var perimeter = 0
        for (digInstruction in input) {
            val nextPosition = when (digInstruction.direction) {
                Direction.R -> Coordinates(currentPosition.x, currentPosition.y + digInstruction.meters)
                Direction.D -> Coordinates(currentPosition.x + digInstruction.meters, currentPosition.y)
                Direction.L -> Coordinates(currentPosition.x, currentPosition.y - digInstruction.meters)
                Direction.U -> Coordinates(currentPosition.x - digInstruction.meters, currentPosition.y)
            }
            perimeter += digInstruction.meters
            visitedCoordinates.add(nextPosition)
            currentPosition = nextPosition
        }

        return abs(getInteriorArea(visitedCoordinates)) - perimeter / 2 + 1 + perimeter
    }

    private fun getInteriorArea(coordinates: List<Coordinates>) =
        coordinates.indices
            .sumOf { i -> coordinates[i].x * coordinates[(i + 1) % (coordinates.size - 1)].y - coordinates[(i + 1) % coordinates.size].x * coordinates[i].y } / 2

    private data class Coordinates(val x: Int, val y: Int)

    private data class DigInstruction(
        val direction: Direction,
        val meters: Int,
        val color: String,
    )

    private enum class Direction {
        R, D, L, U
    }
}