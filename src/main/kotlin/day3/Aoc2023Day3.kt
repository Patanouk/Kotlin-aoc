package day3

import readInput

object Aoc2023Day3 {

    fun solveFirstStar(): Int {
        val input = readInput("/day3/input.txt")
            .map { it.toCharArray().toList() }
        var result = 0

        var currentNumber = 0
        val currentNumberCoordinates = mutableListOf<Coordinates>()

        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j].isDigit()) {
                    currentNumberCoordinates.add(Coordinates(i, j))
                    currentNumber = currentNumber * 10 + input[i][j].digitToInt()
                }

                // We're at the end of the line, or the current input is not a digit
                if (j == input[0].size - 1 || !input[i][j].isDigit()) {
                    currentNumberCoordinates
                        .takeIf { coordinates -> coordinates.any {hasSymbolNeighbour(input, it)} }
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

    data class Coordinates(
        val i: Int,
        val j: Int,
    )
}