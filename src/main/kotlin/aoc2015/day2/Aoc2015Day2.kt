package aoc2015.day2

import readInput

object Aoc2015Day2 {

    fun solveFirstStar(): Int {
        return readInput("/aoc2015/day2/input.txt")
            .map { it.split('x').map { it.toInt() } }
            .sumOf { 2 * (it[0] * it[1] + it[0] * it[2] + it[1] * it[2]) + getSmallestArea(it) }
    }

    fun solveSecondStar(): Int {
        return readInput("/aoc2015/day2/input.txt")
            .map { it.split('x').map { it.toInt() } }
            .sumOf { 2 * listOf(it[0] + it[1], it[0] + it[2], it[1] + it[2]).min() + it.reduce(Int::times) }
    }

    private fun getSmallestArea(dimensions: List<Int>): Int {
        return dimensions.sorted()
            .let { it[0] * it[1] }
    }
}