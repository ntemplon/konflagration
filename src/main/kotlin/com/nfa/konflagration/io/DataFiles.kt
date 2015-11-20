package com.nfa.konflagration.io

import com.nfa.konflagration.math.Point
import java.nio.file.Files
import java.nio.file.Path
import kotlin.text.Regex

public fun readPointsFile(path: Path): List<Point> {
    val lines = Files.readAllLines(path)
    val whitespace = Regex("[\\s\\,]+")
    return lines.map { line ->
        line.split(whitespace)
                .filter { it.length > 0 }
                .map { it.toDouble() }
    }.map { Point(it[0], it[1]) }
}