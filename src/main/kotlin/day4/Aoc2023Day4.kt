package day4

import readInput
import kotlin.math.pow

object Aoc2023Day4 {

    fun solveFirstStar(): Int {
        return readInput("/day4/input.txt")
            .asSequence()
            .map { it.substringAfter(":") }
            .map { it.trim().split("|") }
            .map { it.map { it.trim().split("\\s+".toRegex()).map { it.toInt() }.toSet() } }
            .map { it[0].intersect(it[1]) }
            .map { it.size }
            .filter { it != 0 }
            .sumOf { 2.toDouble().pow(it - 1) }
            .toInt()
    }

}