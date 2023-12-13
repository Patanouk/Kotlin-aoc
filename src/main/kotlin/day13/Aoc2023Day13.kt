package day13

import readInputRaw

object Aoc2023Day13 {

    fun solveFirstStar(): Int {
        return readInputRaw("/day13/input.txt")
            .split("\\n\\n".toRegex())
            .sumOf { findHorizontalReflection(it).sumOf { 100 * (it + 1) } + findHorizontalReflection(rotatePattern(it)).sumOf { it + 1 } }
    }

    fun solveSecondStar(): Int {
        return readInputRaw("/day13/input.txt")
            .split("\\n\\n".toRegex())
            .sumOf { findHorizontalReflection(it, exactDifferences = 1).sumOf { 100 * (it + 1) } + findHorizontalReflection(rotatePattern(it), exactDifferences = 1).sumOf { it + 1 } }
    }

    private fun rotatePattern(pattern: String): String {
        val patternList = pattern.lines()
            .map { it.toList() }

        val result = mutableListOf<List<Char>>()

        for (j in patternList[0].indices) {
            result.add(patternList.map { it[j] }.reversed())
        }

        return result.map { it.joinToString("") }
            .joinToString("\n")
    }

    private fun findHorizontalReflection(pattern: String, exactDifferences: Int = 0): List<Int> {
        val lines = pattern.lines()
        val horizontalReflectionIndex = mutableListOf<Int>()
        for (i in 0..lines.size - 2) {
            var j = 0
            var currentDifferences = 0

            while (true) {
                if (i - j < 0 || i + j + 1 > lines.indices.last) {
                    if (currentDifferences == exactDifferences) {
                        horizontalReflectionIndex.add(i)
                    }
                    break
                }

                if (lines[i - j] != lines[i + j + 1]) {
                    currentDifferences += lines[i - j].filterIndexed { index, c -> lines[i + j + 1][index] != c }.count()
                }

                if (currentDifferences > exactDifferences) {
                    break
                }

                j++
            }
        }

        return horizontalReflectionIndex
    }
}