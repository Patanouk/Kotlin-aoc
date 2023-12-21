package aoc2023.day18

import readInput
import kotlin.math.abs

object Aoc2023Day18 {

    fun solveFirstStar(): Long {
        val input = readInput("/aoc2023/day18/input.txt")
            .map { it.split(' ') }
            .map { DigInstruction(Direction.valueOf(it[0]), it[1].toLong()) }

        return area(input)
    }

    fun solveSecondStar(): Long {
        val input = readInput("/aoc2023/day18/input.txt")
            .map { it.split(' ') }
            .map { it[2].removeSurrounding("(#", ")") }
            .map { DigInstruction(Direction.from(it.last()), it.substring(0, it.length - 1).toLong(radix = 16)) }

        return area(input)
    }

    private fun getInteriorArea(coordinates: List<Coordinates>) =
        coordinates.indices
            .sumOf { i -> coordinates[i].x * coordinates[(i + 1) % (coordinates.size - 1)].y - coordinates[(i + 1) % coordinates.size].x * coordinates[i].y } / 2

    private data class Coordinates(val x: Long, val y: Long)

    private data class DigInstruction(
        val direction: Direction,
        val meters: Long,
    )

    private fun area(input: List<DigInstruction>): Long {
        var currentPosition = Coordinates(0, 0)
        val visitedCoordinates = mutableListOf(currentPosition)

        var perimeter = 0L
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

    private enum class Direction(private val c: Char) {
        R('0'), D('1'), L('2'), U('3');

        companion object {
            fun from(c: Char): Direction {
                return entries.first { it.c == c}
            }
        }
    }
}