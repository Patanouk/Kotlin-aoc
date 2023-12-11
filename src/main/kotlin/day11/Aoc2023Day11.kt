package day11

import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Aoc2023Day11 {


    fun solveFirstStar(): Int {
        val input = readInput("/day11/input.txt")
            .map { it.toList() }

        val emptyLines = input.indices
            .filter { rowIndex -> input[rowIndex].all { it == '.' } }

        val emptyColumns = input[0].indices
            .filter { columnIndex -> input.all { it[columnIndex] == '.' } }


        val galaxies = mutableListOf<Pair<Int, Int>>()
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] == '#') galaxies.add(Pair(i, j))
            }
        }

        return galaxies.mapIndexed { index, galaxy ->
            galaxies.subList(index, galaxies.size).sumOf { otherGalaxy ->
                getDistanceBetweenGalaxies(galaxy, otherGalaxy) +
                        countEmptyLinesBetweenGalaxies(galaxy, otherGalaxy, emptyLines) +
                        countEmptyColumnsBetweenGalaxies(galaxy, otherGalaxy, emptyColumns)
            }
        }.sum()
    }

    private fun countEmptyLinesBetweenGalaxies(g1: Pair<Int, Int>, g2: Pair<Int, Int>, emptyLines: List<Int>) =
        emptyLines.count {
            IntRange(
                min(g1.first, g2.first),
                max(g1.first, g2.first)
            ).contains(it)
        }

    private fun countEmptyColumnsBetweenGalaxies(g1: Pair<Int, Int>, g2: Pair<Int, Int>, emptyColumns: List<Int>) =
        emptyColumns.count {
            IntRange(
                min(g1.second, g2.second),
                max(g1.second, g2.second)
            ).contains(it)
        }

    private fun getDistanceBetweenGalaxies(g1: Pair<Int, Int>, g2: Pair<Int, Int>) =
        abs(g2.first - g1.first) + abs(g2.second - g1.second)
}