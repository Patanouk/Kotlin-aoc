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
        countConsecutiveDamagedSprings(springs, stopFirstUnknown = true)
            .filterIndexed { index, damagedGroup -> damagedGroup != damagedGroups.getOrNull(index) }
            .firstOrNull()
            ?.let { return 0 }


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

    private fun countConsecutiveDamagedSprings(springs: String, stopFirstUnknown: Boolean = false): List<Int> {
        var consecutiveDamagedSprings = 0
        val groupDamagedSprings = mutableListOf<Int>()

        for (index in springs.indices) {
            val spring = springs[index]

            if (stopFirstUnknown && spring == '?') {
                return groupDamagedSprings
            }

            if (spring == '#') {
                consecutiveDamagedSprings++

                if (index == springs.indices.last) {
                    groupDamagedSprings.add(consecutiveDamagedSprings)
                }
                continue
            }

            if (consecutiveDamagedSprings > 0 && spring == '.') {
                groupDamagedSprings.add(consecutiveDamagedSprings)
                consecutiveDamagedSprings = 0
            } else if (spring == '?') {
                consecutiveDamagedSprings = 0
            }
        }

        return groupDamagedSprings
    }

}