package aoc2023.day16

import readInput

object Aoc2023Day16 {

    private val input = readInput("/aoc2023/day16/input.txt")
        .map { it.toCharArray() }

    fun solveFirstStar(): Int {
        return countEnergizedCoordinates(Coordinates(0,0), Direction.Right)
    }

    fun solveSecondStar(): Int {
        val maxRight = input.indices.maxOf { countEnergizedCoordinates(Coordinates(it, 0), Direction.Right) }
        val maxLeft = input.indices.maxOf { countEnergizedCoordinates(Coordinates(it, input[0].indices.last), Direction.Left) }
        val maxDown = input[0].indices.maxOf { countEnergizedCoordinates(Coordinates(0, it), Direction.Down) }
        val maxUp = input[0].indices.maxOf { countEnergizedCoordinates(Coordinates(input.indices.last, it), Direction.Up) }

        return listOf(maxRight, maxLeft, maxDown, maxUp).max()
    }

    private fun countEnergizedCoordinates(startingPos: Coordinates, direction: Direction): Int {
        return fillEnergizedCoordinates(startingPos, direction, mutableSetOf())
            .map { it.first }
            .distinct()
            .count()
    }

    private fun fillEnergizedCoordinates(currentPos: Coordinates, direction: Direction, energizedCoordinates: MutableSet<Pair<Coordinates, Direction>>): MutableSet<Pair<Coordinates, Direction>> {
        if (!input.indices.contains(currentPos.x) || !input[0].indices.contains(currentPos.y) || energizedCoordinates.contains(Pair(currentPos, direction))) {
            return energizedCoordinates
        }
        energizedCoordinates.add(Pair(currentPos, direction))

        val currentChar = input[currentPos.x][currentPos.y]

        when (currentChar) {
            '.' -> {
                fillEnergizedCoordinates(getNextCoordinate(currentPos, direction), direction, energizedCoordinates)
            }
            '/' -> {
                when (direction) {
                    Direction.Up -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Right), Direction.Right, energizedCoordinates)
                    Direction.Down -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Left), Direction.Left , energizedCoordinates)
                    Direction.Left -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Down), Direction.Down, energizedCoordinates)
                    Direction.Right -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Up), Direction.Up, energizedCoordinates)
                }
            }
            '\\' -> {
                when (direction) {
                    Direction.Up -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Left), Direction.Left, energizedCoordinates)
                    Direction.Down -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Right), Direction.Right, energizedCoordinates)
                    Direction.Left -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Up), Direction.Up, energizedCoordinates)
                    Direction.Right -> fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Down), Direction.Down, energizedCoordinates)
                }
            }
            '|' -> {
                when (direction) {
                    Direction.Right,
                    Direction.Left -> {
                        fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Up), Direction.Up, energizedCoordinates)
                        fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Down), Direction.Down, energizedCoordinates)
                    }
                    else -> fillEnergizedCoordinates(getNextCoordinate(currentPos, direction), direction, energizedCoordinates)
                }
            }
            '-' -> {
                when (direction) {
                    Direction.Up,
                    Direction.Down -> {
                        fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Left), Direction.Left, energizedCoordinates)
                        fillEnergizedCoordinates(getNextCoordinate(currentPos, Direction.Right), Direction.Right, energizedCoordinates)
                    }
                    else -> fillEnergizedCoordinates(getNextCoordinate(currentPos, direction), direction, energizedCoordinates)
                }
            }
        }

        return energizedCoordinates
    }

    private fun getNextCoordinate(currentPos: Coordinates, direction: Direction) = when(direction) {
        Direction.Up -> Coordinates(currentPos.x - 1, currentPos.y)
        Direction.Down -> Coordinates(currentPos.x + 1, currentPos.y)
        Direction.Left -> Coordinates(currentPos.x, currentPos.y - 1)
        Direction.Right -> Coordinates(currentPos.x, currentPos.y + 1)
    }

    private enum class Direction {
        Up,
        Down,
        Left,
        Right,
    }

    private data class Coordinates(
        val x: Int,
        val y: Int,
    )
}