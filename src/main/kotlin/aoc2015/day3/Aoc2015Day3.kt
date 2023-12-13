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

    fun solveSecondStar(): Int {
        var santaCurrentPosition = Pair(0, 0)
        val santaVisitedPositions = mutableSetOf(santaCurrentPosition)


        var robotSantaCurrentPosition = Pair(0, 0)
        val robotSantaVisitedPositions = mutableSetOf(santaCurrentPosition)

        readInputRaw("/aoc2015/day3/input.txt").forEachIndexed { index, c ->
            if (index % 2 == 0) {
                santaCurrentPosition = when (c) {
                    '>' -> Pair(santaCurrentPosition.first, santaCurrentPosition.second + 1)
                    '<' -> Pair(santaCurrentPosition.first, santaCurrentPosition.second - 1)
                    'v' -> Pair(santaCurrentPosition.first + 1, santaCurrentPosition.second)
                    '^' -> Pair(santaCurrentPosition.first - 1, santaCurrentPosition.second)
                    else -> throw IllegalArgumentException("Unknown char $c")
                }
                santaVisitedPositions.add(santaCurrentPosition)
            } else {
                robotSantaCurrentPosition = when (c) {
                    '>' -> Pair(robotSantaCurrentPosition.first, robotSantaCurrentPosition.second + 1)
                    '<' -> Pair(robotSantaCurrentPosition.first, robotSantaCurrentPosition.second - 1)
                    'v' -> Pair(robotSantaCurrentPosition.first + 1, robotSantaCurrentPosition.second)
                    '^' -> Pair(robotSantaCurrentPosition.first - 1, robotSantaCurrentPosition.second)
                    else -> throw IllegalArgumentException("Unknown char $c")
                }
                robotSantaVisitedPositions.add(robotSantaCurrentPosition)
            }
        }

        return santaVisitedPositions.union(robotSantaVisitedPositions).size
    }
}