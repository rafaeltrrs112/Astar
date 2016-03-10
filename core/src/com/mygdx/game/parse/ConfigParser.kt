package com.mygdx.game.parse

import com.mygdx.game.algorithm.Point
import java.io.File
import java.util.*

data class ConfigPoint(val posit: Point, val stepType: StepType, val weight: Int, val id : String) {

    enum class StepType {
        TELEPORTER, BARRIER, NORMAL;

        companion object {
            fun fromString(s: String): StepType {

                val isPortal = s.contains(Regex("T"))
                val isBarrier = s.contains(Regex("F"))

                if (isPortal) return StepType.TELEPORTER

                if (isBarrier) return StepType.BARRIER

                return StepType.NORMAL

            }
        }
    }


}

data class ParsedLayout(val layout: List<ConfigPoint>) {
    fun fromPoint(x: Int, y: Int): ConfigPoint {
        val config = layout.find { config ->
            when (config.posit) {
                Point(x, y) -> true
                else -> false
            }
        }
        return when (config) {
            null -> throw AssertionError("Invalid point passed $x, $y")
            else -> config
        }
    }
}

/**
 * Parse for the settings file.
 */
object ConfigParser {
    val numbers = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9')

    fun parseSettings(): List<List<ConfigPoint>> {
        val file = File("config.txt")
        val points: MutableList<List<Pair<String, ConfigPoint.StepType>>> = ArrayList()

        file.forEachLine { line ->
            val splitLines = line.split(" ")
            points.add(splitLines.map { s ->
                Pair(s, ConfigPoint.StepType.fromString(s))
            })
        }

        points.reverse()

        var x = 0
        var y = 0

        val cardinal: List<List<Point>> = points.map { row ->
            val currY = y++
            val ret = row.map { column ->
                val currX = x++
                Point(currX, currY)
            }
            x = 0
            ret
        }

        val nodeType = points.flatten().iterator()
        assert(cardinal.size == points.size)

        val configs = cardinal.map { row ->
            row.map { posit ->
                val srcType = nodeType.next()
                val weight = when (srcType.second) {
                    ConfigPoint.StepType.TELEPORTER -> 0
                    ConfigPoint.StepType.BARRIER -> Int.MAX_VALUE
                    else -> srcType.first.toInt()
                }
                ConfigPoint(posit, srcType.second, weight, srcType.first)
            }
        }

        //        configs.forEach { c -> println(c.map { x -> Pair(x.posit, x.weight) }) }
        return configs
    }

}
