package day6

import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object Aoc2023Day6 {

    fun solveFirstStar(): Int {
        val input = readInput("/day6/input.txt")

        val times = input.first()
            .substringAfter("Time: ")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toInt() }

        val maxDistance = input.last()
            .substringAfter("Distance: ")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toInt() }

        return times.zip(maxDistance)
            .map { Race(it.first, it.second) }
            .map { it.numberWinningWays() }
            .reduce {acc, int -> acc * int}
    }

    private data class Race(
        val time: Int,
        val maxDistance: Int,
    ) {
        fun numberWinningWays(): Int {
            val discriminant = time * time - 4 * maxDistance

            if (discriminant < 0) {
                return 0
            }

            if (discriminant == 0 && time % 2 == 0) {
                return 1
            }

            val zero1 = (time + sqrt(discriminant.toDouble())) / 2
            val zero2 = (time - sqrt(discriminant.toDouble())) / 2

            var result = floor(zero1).toInt() -  floor(zero2).toInt()
            if (ceil(zero1) == floor(zero1)) result -= 1

            return result
        }
    }

//    i(time - i) > maxDistance
//    i * time - i^2 > maxDistance
//
//    -i^2 + i * time - maxDistance > 0
//
//    -i^2 + i * time - maxDistance = 0
//
//    time ^ 2 - 4 * (-1) * (- maxDistance)
//    time ^ 2 - 4maxDistance
}