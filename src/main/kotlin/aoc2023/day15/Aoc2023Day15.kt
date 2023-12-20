package aoc2023.day15

import readInput

object Aoc2023Day15 {

    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day15/input.txt")

        return input.first()
            .split(',')
            .sumOf { hash(it) }
    }

    private fun hash(s: String): Int {
        var result = 0
        s.forEach {
            result = ((result + it.code) * 17) % 256
        }

        return result
    }
}