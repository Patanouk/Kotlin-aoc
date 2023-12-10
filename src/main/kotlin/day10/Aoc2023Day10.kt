package day10

import readInput
import java.lang.IllegalArgumentException

object Aoc2023Day10 {


    fun solveFirstStar(): Int {
        val input = readInput("/day10/input.txt")
            .map { it.toCharArray() }

        val startingPosition = input
            .mapIndexed { index, chars -> Pair(index, chars.indexOf('S')) }
            .first { it.second != -1 }

        var currentTile = startingPosition
        var nextTile = getConnectedTiles(startingPosition, input).first()
        var result = 1

        while (nextTile != startingPosition) {
            val tempTile = getNextTile(currentTile, nextTile, input)

            currentTile = nextTile
            nextTile = tempTile
            result ++
        }
        return result / 2
    }

    private fun getNextTile(previousTile: Pair<Int, Int>, currentTile: Pair<Int, Int>, input: List<CharArray>): Pair<Int, Int> {
        return getConnectedTiles(currentTile, input)
            .filterNot { it == previousTile }
            .first()
    }

    private fun getConnectedTiles(tilePosition: Pair<Int, Int>, input: List<CharArray>): List<Pair<Int, Int>> {
        val connectedTiles = when (input[tilePosition.first][tilePosition.second]) {
            '|' -> listOf(
                Pair(tilePosition.first - 1, tilePosition.second),
                Pair(tilePosition.first + 1, tilePosition.second),
            )

            '-' -> listOf(
                Pair(tilePosition.first, tilePosition.second - 1),
                Pair(tilePosition.first, tilePosition.second + 1),
            )

            'L' -> listOf(
                Pair(tilePosition.first - 1, tilePosition.second),
                Pair(tilePosition.first, tilePosition.second + 1),
            )

            'J' -> listOf(
                Pair(tilePosition.first - 1, tilePosition.second),
                Pair(tilePosition.first, tilePosition.second - 1),
            )

            '7' -> listOf(
                Pair(tilePosition.first + 1, tilePosition.second),
                Pair(tilePosition.first, tilePosition.second - 1),
            )

            'F' -> listOf(
                Pair(tilePosition.first + 1, tilePosition.second),
                Pair(tilePosition.first, tilePosition.second + 1),
            )

            '.' -> listOf()
            'S' -> getAllConnectedTiles(tilePosition, input)
            else -> throw IllegalArgumentException("Unknown tile shape")
        }

        return connectedTiles.filter { tileIsInGrid(it, input) }
    }

    private fun getAllConnectedTiles(startingPosition: Pair<Int, Int>, input: List<CharArray>): MutableList<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        for (i in startingPosition.first - 1..startingPosition.first + 1) {
            for (j in startingPosition.second - 1..startingPosition.second + 1) {
                val connectedTile = Pair(i, j)
                if (connectedTile != startingPosition &&
                    tileIsInGrid(connectedTile, input)
                    && getConnectedTiles(connectedTile, input).contains(startingPosition)) {
                    result.add(connectedTile)
                }
            }
        }

        return result
    }

    private fun tileIsInGrid(tilePosition: Pair<Int, Int>, input: List<CharArray>): Boolean {
        return input.indices.contains(tilePosition.first) && input[0].indices.contains(tilePosition.second)
    }
}