package aoc2023.day14

import readInput

object Aoc2023Day14 {

    fun solveFirstStar(): Int {
        val input = readInput("/aoc2023/day14/input.txt")

        return countBeamLoad(input.map { it.toCharArray() })
    }

    private fun countBeamLoad(input: List<CharArray>): Int {
        var result = 0
        for (i in input.indices) {
            for (j in input[0].indices) {
                if (input[i][j] != 'O') {
                    continue
                }

                val fallPosition = input.subList(0, i)
                    .map { it[j] }
                    .mapIndexed { index, c -> if (c == '#') index + 1 else -1 }
                    .filter { it != -1 }
                    .maxOrNull() ?: 0

                val numberOfRocksBetween = input.subList(fallPosition, i)
                    .count { it[j] == 'O' }

                result += input.size - fallPosition - numberOfRocksBetween
            }
        }
        return result
    }

    fun solveSecondStar(): Int {
        var input = readInput("/aoc2023/day14/input.txt")
            .map { it.toCharArray() }
            .toTypedArray()

        for (i in 0..1000000000) {
            println(i)
            input = rotateNorth(input)
            input = rotateWest(input)
            input = rotateSouth(input)
            input = rotateEast(input)
        }

        return countBeamLoad(input.toList())
    }

    private fun rotateNorth(input: Array<CharArray>): Array<CharArray> {
        for (j in input[0].indices) {
            for (i in input.indices) {
                if (input[i][j] != 'O') {
                    continue
                }

                val fallPosition = input.filterIndexed { index, _ -> index < i }
                    .map { it[j] }
                    .mapIndexed { index, c -> if (c == '#') index + 1 else -1 }
                    .filter { it != -1 }
                    .maxOrNull() ?: 0

                val numberOfRocksInBetween = input.filterIndexed { index, _ -> index in fallPosition..<i }
                    .count { it[j] == 'O' }

                input[i][j] = '.'
                input[fallPosition + numberOfRocksInBetween][j] = 'O'
            }
        }
        return input
    }

    private fun rotateWest(input: Array<CharArray>): Array<CharArray> {
        for (j in input[0].indices) {
            for (i in input.indices) {
                if (input[i][j] != 'O') {
                    continue
                }

                val fallPosition = input[i]
                    .filterIndexed { index, _ -> index < j }
                    .mapIndexed { index, c -> if (c == '#') index + 1 else -1 }
                    .filter { it != -1 }
                    .maxOrNull() ?: 0

                val numberOfRocksInBetween = input[i].filterIndexed { index, _ -> index in fallPosition..<j }
                    .count { it == 'O' }

                input[i][j] = '.'
                input[i][fallPosition + numberOfRocksInBetween] = 'O'
            }
        }
        return input
    }

    private fun rotateSouth(input: Array<CharArray>): Array<CharArray> {
        input.indices.reversed()
        for (j in input[0].indices) {
            for (i in input.indices.reversed()) {
                if (input[i][j] != 'O') {
                    continue
                }

                input.mapIndexed{index, chars -> index > if (chars[j] == '#') index + 1 else -1}

                val fallPosition = input.filterIndexed { index, _ -> index > i }
                    .map { it[j] }
                    .mapIndexed { index, c -> if (c == '#') index + i else -1 }
                    .filter { it != -1 }
                    .minOrNull() ?: input.indices.last

                val numberOfRocksInBetween = input.filterIndexed { index, _ -> index in i+1..fallPosition }
                    .count { it[j] == 'O' }

                input[i][j] = '.'
                input[fallPosition - numberOfRocksInBetween][j] = 'O'
            }
        }
        return input
    }

    private fun rotateEast(input: Array<CharArray>): Array<CharArray> {
        for (j in input[0].indices.reversed()) {
            for (i in input.indices) {
                if (input[i][j] != 'O') {
                    continue
                }

                val fallPosition = input[i]
                    .filterIndexed { index, _ -> index > j }
                    .mapIndexed { index, c -> if (c == '#') index + j else -1 }
                    .filter { it != -1 }
                    .minOrNull() ?: input[0].indices.last

                val numberOfRocksInBetween = input[i].filterIndexed { index, _ -> index in j+1..fallPosition }
                    .count { it == 'O' }

                input[i][j] = '.'
                input[i][fallPosition - numberOfRocksInBetween] = 'O'
            }
        }
        return input
    }
}