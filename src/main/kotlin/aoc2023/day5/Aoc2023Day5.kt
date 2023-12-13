package aoc2023.day5

import readInput

object Aoc2023Day5 {

    fun solveFirstStar(): Long {
        val input = readInput("/aoc2023/day5/input.txt")

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

    fun solveSecondStar(): Long {
        val input = readInput("/aoc2023/day5/input.txt")

        val seeds = input.first()
            .substringAfter("seeds: ")
            .trim()
            .split("\\s+".toRegex())
            .map { it.toLong() }
            .chunked(2)
            .map { LongRange(it[0], it[0] + it[1] - 1) }
            .toSet()

        val previousRanges = seeds.toMutableSet()
        val nextRanges = mutableSetOf<LongRange>()

        for (line in input.subList(2, input.size)) {
            if (line.endsWith("map:")) {
                continue
            }

            if (line.isEmpty()) {
                nextRanges.addAll(previousRanges)
                previousRanges.clear()

                previousRanges.addAll(nextRanges)
                nextRanges.clear()
                continue
            }

            val rangeWithDestination = line.trim()
                .split("\\s+".toRegex())
                .map { it.toLong() }
                .let { RangeWithDestination(sourceStart = it[1], destinationStart = it[0], length = it[2]) }

            val previousRangesToRemove = mutableSetOf<LongRange>()
            val previousRangesToAdd = mutableSetOf<LongRange>()

            previousRanges.forEach{ previousRange ->
                if (rangeWithDestination.contains(previousRange.first) || rangeWithDestination.contains(previousRange.last)) {
                    val minRangeToMap = previousRange.first.coerceAtLeast(rangeWithDestination.sourceStart)
                    val maxRangeToMap = previousRange.last.coerceAtMost(rangeWithDestination.sourceEnd)

                    nextRanges.add(LongRange(
                        rangeWithDestination.destinationStart + (minRangeToMap - rangeWithDestination.sourceStart),
                        rangeWithDestination.destinationStart + (maxRangeToMap - rangeWithDestination.sourceStart),
                    ))

                    previousRangesToRemove.add(previousRange)
                    if (previousRange.first < rangeWithDestination.sourceStart)
                        previousRangesToAdd.add(LongRange(previousRange.first, rangeWithDestination.sourceStart - 1))

                    if (previousRange.last > rangeWithDestination.sourceEnd)
                        previousRangesToAdd.add(LongRange(rangeWithDestination.sourceEnd + 1, previousRange.last))
                }
            }

            previousRanges.removeAll(previousRangesToRemove.toSet())
            previousRanges.addAll(previousRangesToAdd)
        }

        return nextRanges.union(previousRanges).minOf { it.first }
    }

    private data class RangeWithDestination(
        val sourceStart: Long,
        val destinationStart: Long,
        val length: Long,
    ) {
        val sourceEnd: Long = sourceStart + length - 1

        fun contains(value: Long): Boolean {
            return sourceStart <= value && value < sourceStart + length
        }

        fun getNewValue(value: Long)= destinationStart + (value - sourceStart)
    }
}