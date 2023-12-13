package aoc2023.day10

import readInput
import java.lang.IllegalArgumentException

object Aoc2023Day10 {


    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day10/input.txt")
            .map { it.toList() }

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

    fun solveSecondStar(): Int {
        val input = readInput("/aoc2023/day10/input.txt")
            .map { it.toList() }

        val startingPosition = input
            .mapIndexed { index, chars -> Pair(index, chars.indexOf('S')) }
            .first { it.second != -1 }

        val startCharValue = getStartTileValue(startingPosition, input)
        val inputWithSReplaced = input.map { it.map { if (it == 'S') startCharValue else it } }

        val loopTiles = getLoopTiles(startingPosition, input)

        var tilesEnclosed = 0

        for (i in inputWithSReplaced.indices) {
            for (j in inputWithSReplaced[0].indices) {
                if (loopTiles.contains(Pair(i,j))) {
                    continue
                }

                val numberOfBoundaryCrossings = inputWithSReplaced[i].toList()
                    .subList(0, j)
                    .filterIndexed {index, _ -> loopTiles.contains(Pair(i, index)) }
                    .count { it == 'F' || it == '7' || it == '|' }

                if (numberOfBoundaryCrossings % 2 == 1) tilesEnclosed++
            }
        }

        return tilesEnclosed
    }

    private fun getStartTileValue(startingPosition: Pair<Int, Int>, input: List<List<Char>>): Char {
        val connectedTiles = getConnectedTiles(startingPosition, input)

        return "|-LJ7F".first { char ->
            getConnectedTiles(
                startingPosition,
                input.map { it.map { if (it == 'S') char else it } }).toSet() == connectedTiles.toSet()
        }
    }

    private fun getLoopTiles(startingPosition: Pair<Int, Int>, input: List<List<Char>>): Set<Pair<Int, Int>> {
        var currentTile = startingPosition
        var nextTile = getConnectedTiles(startingPosition, input).first()

        val loopTiles = mutableSetOf<Pair<Int, Int>>()
        loopTiles.add(currentTile)
        while (nextTile != startingPosition) {
            loopTiles.add(nextTile)

            val tempTile = getNextTile(currentTile, nextTile, input)
            currentTile = nextTile
            nextTile = tempTile
        }

        return loopTiles
    }

    private fun getNextTile(previousTile: Pair<Int, Int>, currentTile: Pair<Int, Int>, input: List<List<Char>>): Pair<Int, Int> {
        return getConnectedTiles(currentTile, input)
            .filterNot { it == previousTile }
            .first()
    }

    private fun getConnectedTiles(tilePosition: Pair<Int, Int>, input: List<List<Char>>): List<Pair<Int, Int>> {
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

    private fun getAllConnectedTiles(startingPosition: Pair<Int, Int>, input: List<List<Char>>): MutableList<Pair<Int, Int>> {
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

    private fun tileIsInGrid(tilePosition: Pair<Int, Int>, input: List<List<Char>>): Boolean {
        return input.indices.contains(tilePosition.first) && input[0].indices.contains(tilePosition.second)
    }
}