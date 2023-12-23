package aoc2023.day20

import readInput
import java.util.LinkedList

object Aoc2023Day20 {

    private val input = readInput("/aoc2023/day20/input.txt")
    private val broadcasterModule = input
        .first { it.contains("broadcaster") }
        .substringAfter('>')
        .trim()
        .split(", ")
        .let { BroadCastModule(it) }


    private val modules = input.filter { !it.contains("broadcaster") }
        .map { it.split(" -> ") }
        .associate { Pair(it[0].substring(1), createModule(it[0][0], it[1].split(", "))) }
        .toMutableMap()

    private val inputs = getInputMap()

    init {
        modules["broadcaster"] = broadcasterModule
        modules.filter { it.value is ConjunctionModule }
            .forEach { (it.value as ConjunctionModule).setInputModules(inputs[it.key]!!) }
    }

    fun solveFirstStar(): Int {

        var lowPulseCount = 0
        var highPulseCount = 0
        for (i in 1..1000) {
//            println("-----------------------------------")
//            println("Cycle $i")

            val pulseCount = pressButton()
            lowPulseCount += pulseCount.first
            highPulseCount += pulseCount.second
        }

        return lowPulseCount * highPulseCount
    }

    private fun pressButton(): Pair<Int, Int> {
        val broadcasts = LinkedList<Broadcast>()
        broadcasts.add(Broadcast("button", "broadcaster", Pulse.Low))

        var lowPulseCount = 0
        var highPulseCount = 0
        while (!broadcasts.isEmpty()) {
            val broadcast = broadcasts.pop()
            val destinationModule = modules.getOrPut(broadcast.destination) { NullModule() }
//            println("${broadcast.input} -${broadcast.pulse}-> ${broadcast.destination}")

            when(broadcast.pulse) {
                Pulse.Low -> lowPulseCount++
                Pulse.High -> highPulseCount++
            }

            val nextPulse = destinationModule.broadcast(broadcast.input, broadcast.pulse) ?: continue
            destinationModule.destinationModules
                .forEach { broadcasts.add(Broadcast(broadcast.destination, it, nextPulse)) }
        }

        return Pair(lowPulseCount, highPulseCount)
    }

    private fun getInputMap(): Map<String, MutableSet<String>> {
        val result = mutableMapOf<String, MutableSet<String>>()

        for (entry in modules) {
            val moduleName = entry.key
            val moduleDestinations = entry.value.destinationModules
            moduleDestinations.forEach {
                val inputs = result.getOrPut(it) { mutableSetOf() }
                inputs.add(moduleName)
            }
        }
        return result
    }

    private fun createModule(type: Char, destinationModules: List<String>): Module {
        return when (type) {
            '%' -> FlipFlopModule(destinationModules)
            '&' -> ConjunctionModule(destinationModules)
            else -> error("Unsupported type")
        }
    }

    private abstract class Module(val destinationModules: List<String>) {
        abstract fun broadcast(input: String, pulse: Pulse): Pulse?
    }

    private class NullModule : Module(listOf()) {
        override fun broadcast(input: String, pulse: Pulse): Pulse? {
            return null
        }
    }

    private class BroadCastModule(destinationModules: List<String>) : Module(destinationModules) {
        override fun broadcast(input: String, pulse: Pulse) = pulse
    }

    private class ConjunctionModule(destinationModules: List<String>) : Module(destinationModules) {
        private val inputState = mutableMapOf<String, Pulse>()

        fun setInputModules(inputModules: Set<String>) {
            inputModules.forEach { inputState[it] = Pulse.Low }
        }

        override fun broadcast(input: String, pulse: Pulse): Pulse {
            inputState[input] = pulse

            return when (inputState.values.all { it == Pulse.High }) {
                true -> Pulse.Low
                false -> Pulse.High
            }
        }
    }

    private class FlipFlopModule(destinationModules: List<String>) : Module(destinationModules) {
        private var state = false

        override fun broadcast(input: String, pulse: Pulse): Pulse? {
            if (pulse == Pulse.High) {
                return null
            }

            val result = when (state) {
                true -> Pulse.Low
                false -> Pulse.High
            }
            state = !state
            return result
        }
    }

    private data class Broadcast(val input: String, val destination: String, val pulse: Pulse)

    private enum class Pulse { Low, High }
}