package AOC2023.day3

import readInput

object Aoc2023Day3 {

    fun solveFirstStar(): Int {
        val input = readInput("/AOC2023/day3/input.txt")
            .map { it.toCharArray().toList() }
        var result = 0

        var currentNumber = 0
        val currentNumberCoordinates = mutableListOf<Coordinates>()

        for (i in input.indices) {
            for (j in input[0].indices) {

                input[i][j]
                    .takeIf { it.isDigit() }
                    ?.let { currentNumberCoordinates.add(Coordinates(i, j)) }
                    ?.let { currentNumber = currentNumber * 10 + input[i][j].digitToInt() }

                // We're at the end of the line, or the current input is not a digit
                if (j == input[0].size - 1 || !input[i][j].isDigit()) {
                    currentNumberCoordinates
                        .takeIf { coordinates -> coordinates.any { hasSymbolNeighbour(input, it) } }
                        ?.let { result += currentNumber }

                    currentNumber = 0
                    currentNumberCoordinates.clear()
                }
            }
        }

        // Check the last number
        if (currentNumberCoordinates.any { hasSymbolNeighbour(input, it) }) {
            result += currentNumber
        }

        return result
    }

    private fun hasSymbolNeighbour(input: List<List<Char>>, coordinates: Coordinates): Boolean {
        for (ii in coordinates.i - 1..coordinates.i + 1) {
            for (jj in coordinates.j - 1..coordinates.j + 1) {
                input.getOrNull(ii)
                    ?.getOrNull(jj)
                    ?.takeUnless { it.isDigit() || it == '.' }
                    ?.let { return true }
            }
        }

        return false
    }

    fun solveSecondStar(): Int {
        val input = readInput("/AOC2023/day3/input.txt")
            .map { it.toCharArray().toList() }
        var result = 0

        var currentNumber = 0
        val currentNumberCoordinates = mutableListOf<Coordinates>()
        val coordinatesToNumber = mutableListOf<NumberWithCoordinates>()

        for (i in input.indices) {
            for (j in input[0].indices) {

                input[i][j]
                    .takeIf { it.isDigit() }
                    ?.let { currentNumberCoordinates.add(Coordinates(i, j)) }
                    ?.let { currentNumber = currentNumber * 10 + input[i][j].digitToInt() }

                currentNumberCoordinates
                    .takeIf { j == input[0].size - 1 || !input[i][j].isDigit() }
                    ?.takeIf { currentNumber != 0 }
                    ?.let { coordinatesToNumber.add(NumberWithCoordinates(currentNumber, it.toList())) }
                    ?.also {
                        currentNumber = 0
                        currentNumberCoordinates.clear()
                    }
            }
        }

        for (i in input.indices) {
            for (j in input[0].indices) {
                result += input[i][j]
                    .takeIf { it == '*' }
                    ?.let { getNeighbouringNumbers(coordinatesToNumber, Coordinates(i, j)) }
                    ?.takeIf { it.size == 2 }
                    ?.let { it[0] * it[1] }
                    ?: 0
            }
        }

        return result
    }

    private fun getNeighbouringNumbers(coordinatesToNumber: List<NumberWithCoordinates>, gearCoordinate: Coordinates): List<Int> {
        return coordinatesToNumber
            .filter { it.coordinates.any { coordinate ->  coordinate.isAdjacent(gearCoordinate) } }
            .map { it.number }
    }

    data class NumberWithCoordinates(
        val number: Int,
        val coordinates: List<Coordinates>
    )

    data class Coordinates(
        val i: Int,
        val j: Int,
    ) {
        fun isAdjacent(c: Coordinates): Boolean {
            return this.i >= c.i - 1
                    && this.i <= c.i + 1
                    && this.j >= c.j - 1
                    && this.j <= c.j + 1
        }
    }
}