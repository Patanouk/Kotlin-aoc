package AOC2023.day4

import readInput
import java.util.*
import kotlin.math.pow

object Aoc2023Day4 {

    fun solveFirstStar(): Int {
        return readInput("/AOC2023/day4/input.txt")
            .asSequence()
            .map { it.substringAfter(":") }
            .map { it.trim().split("|") }
            .map { it.map { it.trim().split("\\s+".toRegex()).map { it.toInt() }.toSet() } }
            .map { it[0].intersect(it[1]) }
            .map { it.size }
            .filter { it != 0 }
            .sumOf { 2.toDouble().pow(it - 1) }
            .toInt()
    }

    fun solveSecondStar(): Int {
        val matchingNumbersPerCard = readInput("/AOC2023/day4/input.txt")
            .asSequence()
            .map { it.substringAfter(":") }
            .map { it.trim().split("|") }
            .map { it.map { it.trim().split("\\s+".toRegex()).map { it.toInt() }.toSet() } }
            .map { it[0].intersect(it[1]) }
            .map { it.size }
            .toList()


        val numberOfCards = Collections.nCopies(matchingNumbersPerCard.size, 1).toMutableList()

        matchingNumbersPerCard
            .forEachIndexed { index: Int, numberMatchingNumbers: Int ->
                IntRange(index + 1, (index + numberMatchingNumbers).coerceAtMost(numberOfCards.size - 1))
                    .forEach { numberOfCards[it] += numberOfCards[index] }
            }

        return numberOfCards.sum()
    }

}