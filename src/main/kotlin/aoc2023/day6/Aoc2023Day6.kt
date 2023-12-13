package aoc2023.day6

import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Aoc2023Day6 {

    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day6/input.txt")

        val times = input.first()
            .substringAfter("Time: ")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toLong() }

        val maxDistance = input.last()
            .substringAfter("Distance: ")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toLong() }

        return times.zip(maxDistance)
            .map { Race(it.first, it.second) }
            .map { it.numberWinningWays() }
            .reduce {acc, int -> acc * int}
    }

    fun solveSecondStar(): Int {
        val input = readInput("/aoc2023/day6/input.txt")

        val time = input.first()
            .substringAfter("Time: ")
            .replace(" ", "")
            .toLong()

        val distance = input.last()
            .substringAfter("Distance: ")
            .replace(" ", "")
            .toLong()

        return Race(time, distance).numberWinningWays()
    }

    private data class Race(
        val time: Long,
        val maxDistance: Long,
    ) {
        fun numberWinningWays(): Int {
            val discriminant = time * time - 4 * maxDistance

            if (discriminant < 0) {
                return 0
            }

            if (discriminant == 0L && time % 2 == 0L) {
                return 1
            }

            val zero1 = (time + sqrt(discriminant.toDouble())) / 2
            val zero2 = (time - sqrt(discriminant.toDouble())) / 2

            var result = floor(zero1).toInt() -  floor(zero2).toInt()
            if (ceil(zero1) == floor(zero1)) result -= 1

            return result
        }
    }
}