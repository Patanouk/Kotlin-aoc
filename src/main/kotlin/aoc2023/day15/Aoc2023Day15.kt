package aoc2023.day15

import readInput

object Aoc2023Day15 {

    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day15/input.txt")

        return input.first()
            .split(',')
            .sumOf { hash(it) }
    }

    private fun hash(s: String): Int {
        var result = 0
        s.forEach {
            result = ((result + it.code) * 17) % 256
        }

        return result
    }

    fun solveSecondStar(): Int {
        val input = readInput("/aoc2023/day15/input.txt")
        val boxes = mutableMapOf<Int, MutableList<Pair<String, Int>>>()

        input.first()
            .split(',')
            .forEach {
                if (it.contains('=')) {
                    val label = it.substringBefore('=')
                    val hash = hash(label)
                    val focalLength = it.substringAfter('=').toInt()

                    val box = boxes.getOrPut(hash) { mutableListOf() }
                    if (box.any { it.first == label }) {
                        box[box.indexOfFirst { it.first == label }] = Pair(label, focalLength)
                    } else {
                        box.add(Pair(label, focalLength))
                    }

                } else {
                    val label = it.substringBefore('-')
                    val hash = hash(label)

                    val box = boxes.getOrPut(hash) { mutableListOf() }
                    if (box.any { it.first == label }) {
                        box.removeAt(box.indexOfFirst { it.first == label })
                    }
                }
            }


        return boxes
            .map { (it.key + 1) * it.value.mapIndexed { index, lens -> (index + 1) * lens.second }.sum() }
            .sum()
    }
}