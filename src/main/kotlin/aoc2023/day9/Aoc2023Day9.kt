package aoc2023.day9

import readInput

object Aoc2023Day9 {


    fun solveFirstStar(): Int {
        return readInput("/aoc2023/day9/input.txt")
            .map { it.split(' ').map { it.toInt() } }
            .sumOf { getNextSequenceValue(it) }
    }

    fun solveSecondStar(): Int {
        return readInput("/aoc2023/day9/input.txt")
            .map { it.split(' ').map { it.toInt() }.reversed() }
            .sumOf { getNextSequenceValue(it) }
    }

    private fun getNextSequenceValue(sequence: List<Int>): Int {
        if (sequence.all { it == 0 }) {
            return 0
        }

        return sequence.last() + getNextSequenceValue(getSequenceDifference(sequence))
    }

    private fun getSequenceDifference(sequence: List<Int>): List<Int> {
        return IntRange(1, sequence.size - 1)
            .map { sequence[it] - sequence[it - 1] }
    }
}