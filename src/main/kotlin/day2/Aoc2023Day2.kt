package day2

import readInput

object Aoc2023Day2 {

    private val MAX_PER_COLOR = mapOf(
        Color.red to 12,
        Color.green to 13,
        Color.blue to 14,
    )

    fun solveFirstStar(): Int {
        val input = readInput("/day2/input-day-2.txt")

        return mapInputToListOfDraw(input)
            .mapIndexed { i, draws -> (i + 1) to draws }
            .filter { (_, draws) ->
                draws.all {
                    MAX_PER_COLOR[it.color]!! >= it.number
                }
            }.sumOf { (i, _) -> i }
    }

    fun solveSecondStar(): Int {
        val input = readInput("/day2/input-day-2.txt")

        return mapInputToListOfDraw(input)
            .map { draws -> draws.groupBy(Draw::color, Draw::number) }
            .map { it.mapValues { entry -> entry.value.max() } }
            .map { it.values }
            .map { it.reduce { acc, i -> acc * i } }
            .sum()
    }

    private fun mapInputToListOfDraw(input: List<String>): List<List<Draw>> {
        return input.map { it.substringAfter(":") }
            .map { it.split(";") }
            .map { it.flatMap { it.split(",") } }
            .map { it.map { it.trim().split(" ") } }
            .map { it.map { Draw(it[0].toInt(), Color.valueOf(it[1])) } }
    }

    private data class Draw(
        val number: Int,
        val color: Color,
    )

    private enum class Color {
        blue,
        red,
        green,
    }
}