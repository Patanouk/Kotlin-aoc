package aoc2015.day1

import readInput
import java.lang.IllegalArgumentException

object Aoc2015Day1 {

    fun solveFirstStar(): Int {
        return readInput("/aoc2015/day1/input.txt")
            .first()
            .map { when(it) {
                '(' -> 1
                ')' -> -1
                else -> throw IllegalArgumentException("Unknown char $it")
            } }
            .sum()
    }

    fun solveSecondStar(): Int {
        val instructions = readInput("/aoc2015/day1/input.txt")
            .first()
            .map {
                when (it) {
                    '(' -> 1
                    ')' -> -1
                    else -> throw IllegalArgumentException("Unknown char $it")
                }
            }

        var floor = 0
        for (i in instructions.indices) {
            floor += instructions[i]

            if (floor == -1) {
                return i + 1
            }
        }

        return -1
    }
}