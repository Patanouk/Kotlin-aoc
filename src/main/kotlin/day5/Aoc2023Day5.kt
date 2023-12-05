package day5

import readInput

object Aoc2023Day5 {

    fun solveFirstStar(): Long {
        val input = readInput("/day5/input.txt")

        val seeds = input.first()
        .substringAfter("seeds: ")
        .trim()
        .split("\\s+".toRegex())
        .map { it.toLong() }
        .toSet()

        val previousSeeds = seeds.toMutableSet()
        val nextSeeds = mutableSetOf<Long>()

        for (line in input.subList(2, input.size)) {
            if (line.endsWith("map:")) {
                continue
            }

            if (line.isEmpty()) {
                nextSeeds.addAll(previousSeeds)
                previousSeeds.clear()

                previousSeeds.addAll(nextSeeds)
                nextSeeds.clear()
                continue
            }

            val rangeWithDestination = line.trim()
                .split("\\s+".toRegex())
                .map { it.toLong() }
                .let { RangeWithDestination(sourceStart = it[1], destinationStart = it[0], length = it[2]) }

            previousSeeds
                .filter { rangeWithDestination.contains(it) }
                .forEach {
                    previousSeeds.remove(it)
                    nextSeeds.add(rangeWithDestination.getNewValue(it))
                }
        }

        return nextSeeds.union(previousSeeds).min()
    }



    private data class RangeWithDestination(
        val sourceStart: Long,
        val destinationStart: Long,
        val length: Long,
    ) {
        fun contains(value: Long): Boolean {
            return sourceStart <= value && value < sourceStart + length
        }

        fun getNewValue(value: Long): Long {
            return destinationStart + (value - sourceStart)
        }

    }

}