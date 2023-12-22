package aoc2023.day19

import readInputRaw

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

    private data class Workflow(val rules: List<Rule>) {

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