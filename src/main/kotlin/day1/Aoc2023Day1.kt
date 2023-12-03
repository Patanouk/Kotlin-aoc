package day1

import readInput

object Aoc2023Day1 {

    fun solveFirstStar(): Int {
            val input = readInput("/day1/input-first-star.txt")

        var result = 0

        for (line in input) {
            val digits = line.filter { it.isDigit() }.toList()

            val firstDigit = digits.firstOrNull()?.digitToInt() ?: 0
            val lastDigit = digits.lastOrNull()?.digitToInt() ?: 0

            result += firstDigit * 10 + lastDigit
        }

        return result
    }

}