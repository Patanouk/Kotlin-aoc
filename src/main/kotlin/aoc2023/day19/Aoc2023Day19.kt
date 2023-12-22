package aoc2023.day19

import readInputRaw
import java.math.BigDecimal
import kotlin.math.max
import kotlin.math.min

object Aoc2023Day19 {

    fun solveFirstStar(): Int {
        val input = readInputRaw("/aoc2023/day19/input.txt").split("\\n\\n".toRegex())
        val workflows = input[0]
            .lines()
            .associate { Pair(it.substringBefore('{'), Workflow.from(it.substringAfter('{').removeSuffix("}"))) }

        val partsRating = input[1].lines()
            .map { it.removeSurrounding("{", "}") }
            .map { it.split(',') }
            .map { it.map { it.split('=') }.associate { Pair(it[0][0], it[1].toInt()) } }

        var result = 0
        for (partRating in partsRating) {
            var currentWorkflow = workflows["in"]!!
            while (true) {
                val nextDestination = currentWorkflow.getNextDestination(partRating)
                when (val nextDestination = nextDestination) {
                    "R" -> break
                    "A" -> {
                        result += partRating.values.sum(); break
                    }
                    else -> currentWorkflow = workflows[nextDestination]!!
                }
            }
        }

        return result
    }

    fun solveSecondStar(): BigDecimal {
        val input = readInputRaw("/aoc2023/day19/input.txt").split("\\n\\n".toRegex())
        val workflows = input[0]
            .lines()
            .associate { Pair(it.substringBefore('{'), Workflow.from(it.substringAfter('{').removeSuffix("}"))) }

        val partRange = mapOf('x' to IntRange(1, 4000), 'm' to IntRange(1, 4000), 'a' to IntRange(1, 4000), 's' to IntRange(1, 4000))

        return countMatchingRange(partRange, "in", workflows)
    }

    private fun countMatchingRange(partsRange: Map<Char, IntRange>, destination: String, workflows: Map<String, Workflow>): BigDecimal {
        if (destination == "R") {
            return BigDecimal.ZERO
        }
        if (destination == "A") {
            return partsRange.values
                .fold(BigDecimal.ONE) {acc, intRange -> acc.multiply(BigDecimal.valueOf((intRange.last - intRange.first + 1).toLong())) }
        }

        val currentWorkflow = workflows[destination]!!
        return currentWorkflow.getNextDestinations(partsRange)
            .map { countMatchingRange(it.second, it.first, workflows) }
            .reduce { acc, bigDecimal -> acc.add(bigDecimal) }
    }

    private data class Workflow(val rules: List<Rule>) {

        fun getNextDestinations(partRatingsRange: Map<Char, IntRange>): MutableList<Pair<String, Map<Char, IntRange>>> {
            val result = mutableListOf<Pair<String, Map<Char, IntRange>>>()

            var nextRange = partRatingsRange
            for (rule in rules) {
                val transformedRange = rule.transform(nextRange)
                if (transformedRange.first != null) {
                    result.add(Pair(rule.workflowDestination, transformedRange.first!!))
                }
                nextRange = transformedRange.second
            }

            return result
        }

        fun getNextDestination(partRatings: Map<Char, Int>): String {
            return rules.first { it.matches(partRatings) }.workflowDestination
        }

        companion object {
            fun from(workflow: String): Workflow {
                return Workflow(workflow.split(",").map { Rule.from(it) })
            }
        }
    }

    private data class Rule(
        val partCategory: Char,
        val comparisonSign: Char,
        val comparisonValue: Int,
        val workflowDestination: String,
    ) {
        fun transform(partRatingsRange: Map<Char, IntRange>): Pair<Map<Char, IntRange>?, Map<Char, IntRange>> {
            if (comparisonSign != '<' && comparisonSign != '>') {
                return Pair(partRatingsRange.toMap(), partRatingsRange.toMap())
            }

            val partRatingRange = partRatingsRange[partCategory]!!
            val result = partRatingsRange.toMutableMap()

            if (comparisonSign == '<' && partRatingRange.first < comparisonValue) {
                result[partCategory] = IntRange(partRatingRange.first, min(comparisonValue - 1, partRatingRange.last))
                val nextRange = result.toMutableMap()
                nextRange[partCategory] = IntRange(comparisonValue, partRatingRange.last)
                return Pair(result, nextRange)
            }

            if (comparisonSign == '>' && partRatingRange.last > comparisonValue) {
                result[partCategory] = IntRange(max(partRatingRange.first, comparisonValue + 1), partRatingRange.last)
                val nextRange = result.toMutableMap()
                nextRange[partCategory] = IntRange(partRatingRange.first, comparisonValue)
                return Pair(result, nextRange)
            }
            return Pair(null, partRatingsRange.toMap())
        }

        fun matches(partRatings: Map<Char, Int>): Boolean {
            val partRating = partRatings.getOrElse(partCategory) { 0 }
            return when (comparisonSign) {
                '>' -> partRating > comparisonValue
                '<' -> partRating < comparisonValue
                else -> true
            }
        }

        companion object {
            fun from(rule: String): Rule {
                return if (rule.contains('>') || rule.contains('<')) {
                    Rule(
                        rule[0],
                        rule[1],
                        rule.substringBefore(':').substring(2).toInt(),
                        rule.substringAfter(':'),
                    )
                } else {
                    Rule('|', '=', Int.MAX_VALUE, rule)
                }
            }
        }
    }
}