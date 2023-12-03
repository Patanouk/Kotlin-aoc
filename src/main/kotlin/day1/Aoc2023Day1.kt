package day1

import readInput

object Aoc2023Day1 {

    fun solveFirstStar(): Int {
        val input = readInput("/day1/input-day-1.txt")
        var result = 0

        for (line in input) {
            val digits = line.filter { it.isDigit() }.map { it.digitToInt() }.toList()
            result += mapDigitListToNumber(digits)
        }

        return result
    }

    fun solveSecondStar(): Int {
        val input = readInput("/day1/input-day-1.txt")
        var result = 0

        for (line in input) {
            val digitsList = mutableListOf<Int>()

            for (i in line.indices) {
                if (line[i].isDigit()) {
                    digitsList.add(line[i].digitToInt())
                    continue
                }

                stringToDigit.filter { line.substring(i).startsWith(it.key) }
                    .map { it.value }
                    .firstOrNull()
                    ?.let { digitsList.add(it) }
            }

            result += mapDigitListToNumber(digitsList)
        }

        return result
    }

    private fun mapDigitListToNumber(digits: List<Int>): Int {
        val firstDigit = digits.firstOrNull() ?: 0
        val lastDigit = digits.lastOrNull() ?: 0

        return firstDigit * 10 + lastDigit
    }

    private val stringToDigit = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )
}