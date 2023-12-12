package day12

import readInput

object Aoc2023Day12 {


    fun solveFirstStar(): Int {
        return readInput("/day12/input.txt")
            .map { it.split(' ') }
            .map { Pair(it[0], it[1].split(',').map { it.toInt() }) }
            .sumOf { getNumberPossibleArrangements(it.first, it.second, mutableMapOf()) }
    }

    fun solveSecondStar(): Int {
        return readInput("/day12/input.txt")
            .map { it.split(' ') }
            .map { listOf((it[0] + '?').repeat(5).dropLast(1), (it[1] + ',').repeat(5).dropLast(1)) }
            .map { Pair(it[0], it[1].split(',').map { it.toInt() }) }
            .sumOf { getNumberPossibleArrangements(it.first, it.second, mutableMapOf()) }
    }

    private fun getNumberPossibleArrangements(springs: String, damagedGroups: List<Int>, memoizationMap: MutableMap<Pair<String, List<Int>>, Int>): Int {
//        println("$springs || $damagedGroups")

        if (memoizationMap.contains(Pair(springs, damagedGroups))) {
            return memoizationMap[Pair(springs, damagedGroups)]!!
        }

        if (springs.all { it != '?' }) {
            return if (countConsecutiveDamagedSprings(springs) == damagedGroups) {
                1
            } else {
                0
            }
        }

        val currentDamagedGroupSizes = countConsecutiveDamagedSprings(springs)
        if (currentDamagedGroupSizes.size > damagedGroups.size
            || currentDamagedGroupSizes
                .filterIndexed { index, currentDamagedGroupSize -> currentDamagedGroupSize != damagedGroups[index] }
                .isNotEmpty()
            || damagedGroups.sum() > springs.count { it != '.' }
        ) {
            return 0
        }

        var truncatedSprings = springs
        val truncatedDamagedGroups = damagedGroups.toMutableList()
        currentDamagedGroupSizes.forEach {
            truncatedSprings = truncatedSprings.substring(truncatedSprings.indexOfFirst { it == '#' } + it)
            truncatedDamagedGroups.removeFirst()
        }

        truncatedSprings = truncatedSprings.substring(truncatedSprings.indexOfFirst { it != '.' })

        val nonDamagedSpring = truncatedSprings.replaceFirst('?', '.')
        val nonDamagedResult = getNumberPossibleArrangements(nonDamagedSpring, truncatedDamagedGroups, memoizationMap)
        val damagedSpring = truncatedSprings.replaceFirst('?', '#')
        val damagedResult = getNumberPossibleArrangements(damagedSpring, truncatedDamagedGroups, memoizationMap)

        memoizationMap[Pair(springs, damagedGroups)] = nonDamagedResult + damagedResult

        return damagedResult + nonDamagedResult
    }

    private fun countConsecutiveDamagedSprings(springs: String): List<Int> {
        var consecutiveDamagedSprings = 0
        val groupDamagedSprings = mutableListOf<Int>()

        for (index in springs.indices) {
            val spring = springs[index]

            if (spring == '?') {
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
            }
        }

        return groupDamagedSprings
    }

}