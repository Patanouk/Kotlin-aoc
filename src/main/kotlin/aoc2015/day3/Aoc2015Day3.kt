package aoc2015.day3

import readInputRaw

object Aoc2015Day3 {

    fun solveFirstStar(): Int {
        var currentPosition = Pair(0, 0)
        val visitedPositions = mutableSetOf(currentPosition)
        for (c in readInputRaw("/aoc2015/day3/input.txt")) {
            currentPosition = when (c) {
                '>' -> Pair(currentPosition.first, currentPosition.second + 1)
                '<' -> Pair(currentPosition.first, currentPosition.second - 1)
                'v' -> Pair(currentPosition.first + 1, currentPosition.second)
                '^' -> Pair(currentPosition.first - 1, currentPosition.second)
                else -> throw IllegalArgumentException("Unknown char $c")
            }
            visitedPositions.add(currentPosition)
        }

        return visitedPositions.size
    }
}