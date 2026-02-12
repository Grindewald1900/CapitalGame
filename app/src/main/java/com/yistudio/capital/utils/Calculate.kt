package com.yistudio.capital.utils

fun formatMoney(v: Double): String {
    // 1_234 -> "1.23K", 12_300_000 -> "12.3M"
    val abs = kotlin.math.abs(v)
    val (value, suffix) = when {
        abs >= 1e30 -> v / 1e30 to "No" // Nonillion
        abs >= 1e27 -> v / 1e27 to "Oc" // Octillion
        abs >= 1e24 -> v / 1e24 to "Sp" // Septillion
        abs >= 1e21 -> v / 1e21 to "Sx" // Sextillion
        abs >= 1e18 -> v / 1e18 to "Qi" // Quintillion
        abs >= 1e15 -> v / 1e15 to "Qa" // Quadrillion
        abs >= 1e12 -> v / 1e12 to "T"  // Trillion
        abs >= 1e9  -> v / 1e9  to "B"  // Billion
        abs >= 1e6  -> v / 1e6  to "M"  // Million
        abs >= 1e3  -> v / 1e3  to "K"  // Thousand
        else        -> v        to ""
    }
    return if (suffix.isEmpty()) {
        "$" + String.format("%,.0f", value)
    } else {
        "$" + String.format("%.2f", value) + suffix
    }
}