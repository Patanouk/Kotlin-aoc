package aoc2023.day14

import readInput

object Aoc2023Day14 {

    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day14/input.txt")

        var result = 0
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] != 'O') {
                    continue
                }

                val fallPosition = input.subList(0, i)
                    .map { it[j] }
                    .mapIndexed { index, c -> if (c == '#') index + 1 else -1 }
                    .filter { it != -1 }
                    .maxOrNull() ?: 0

                val numberOfRocksBetween = input.subList(fallPosition, i)
                    .count { it[j] == 'O' }

                result += input.size - fallPosition - numberOfRocksBetween
            }
        }

        return result
    }
}