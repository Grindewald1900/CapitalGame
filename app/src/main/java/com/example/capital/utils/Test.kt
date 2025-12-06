package com.example.capital.utils

import org.jsoup.Jsoup
import java.net.URL

fun printSecretMessageGrid(docUrl: String) {
    // 1. Fetch the published Google Doc HTML
    val html = URL(docUrl).readText()

    // 2. Parse visible text with Jsoup
    val doc = Jsoup.parse(html)
    val tokens = doc.body().text().split(Regex("\\s+"))

    // 3. Find where the data starts (right after "y-coordinate")
    val headerEndIndex = tokens.indexOf("y-coordinate")
    if (headerEndIndex == -1) {
        error("Could not find header 'y-coordinate' in document.")
    }

    // 4. Read triples: x, char, y
    //    tokens: [ ..., "x-coordinate", "Character", "y-coordinate", x1, c1, y1, x2, c2, y2, ... ]
    val triples = mutableListOf<Triple<Int, Char, Int>>()
    var i = headerEndIndex + 1

    while (i + 2 < tokens.size) {
        val x = tokens[i].toIntOrNull() ?: break          // x-coordinate
        val charToken = tokens[i + 1]                     // character (Unicode)
        val y = tokens[i + 2].toIntOrNull() ?: break      // y-coordinate

        // We assume a single visible character (as in the example doc)
        val ch = charToken.first()

        triples += Triple(x, ch, y)
        i += 3
    }

    if (triples.isEmpty()) {
        println("No character data found.")
        return
    }

    // 5. Determine grid size
    val maxX = triples.maxOf { it.first }
    val maxY = triples.maxOf { it.third }

    // 6. Create grid filled with spaces
    val grid = Array(maxY + 1) { CharArray(maxX + 1) { ' ' } }

    // 7. Place characters into the grid (y = row, x = column)
    for ((x, ch, y) in triples) {
        grid[y][x] = ch
    }

    // 8. Print the grid; with a fixed-width font this shows the secret message
    for (row in grid) {
        println(row.concatToString())
    }
}