package day12

import readInput

object Aoc2023Day12 {


    fun solveFirstStar(): Int {
        return readInput("/day12/input.txt")
            .map { it.split(' ') }
            .map { Pair(it[0], it[1].split(',').map { it.toInt() }) }
            .sumOf { getNumberPossibleArrangements(it.first, it.second) }
    }

    private fun getNumberPossibleArrangements(springs: String, damagedGroups: List<Int>): Int {
        // There is an early exit we can add here, but the code is fast enough as is

        if (springs.all { it != '?' }) {
            return if (countConsecutiveDamagedSprings(springs) == damagedGroups) {
                1
            } else {
                0
            }
        }

        return getNumberPossibleArrangements(springs.replaceFirst('?', '.'), damagedGroups) +
                getNumberPossibleArrangements(springs.replaceFirst('?', '#'), damagedGroups)
    }

    private fun countConsecutiveDamagedSprings(springs: String): List<Int> {
        var consecutiveDamagedSprings = 0
        val groupDamagedSprings = mutableListOf<Int>()

        for (spring in springs) {
            if (spring == '#') {
                consecutiveDamagedSprings++
                continue
            }

            if (consecutiveDamagedSprings > 0) {
                groupDamagedSprings.add(consecutiveDamagedSprings)
                consecutiveDamagedSprings = 0
            }
        }

        if (consecutiveDamagedSprings > 0) groupDamagedSprings.add(consecutiveDamagedSprings)
        return groupDamagedSprings
    }

}