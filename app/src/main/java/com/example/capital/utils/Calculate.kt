package com.example.capital.utils

fun formatMoney(v: Double): String {
    // 1_234 -> "1.23K", 12_300_000 -> "12.3M"
    val abs = kotlin.math.abs(v)
    val (value, suffix) = when {
        abs >= 1_000_000_000_000.0 -> v / 1_000_000_000_000.0 to "T"
        abs >= 1_000_000_000.0     -> v / 1_000_000_000.0     to "B"
        abs >= 1_000_000.0         -> v / 1_000_000.0         to "M"
        abs >= 1_000.0             -> v / 1_000.0             to "K"
        else                       -> v                       to ""
    }
    return if (suffix.isEmpty()) {
        "$" + String.format("%,.0f", value)
    } else {
        "$" + String.format("%.2f", value) + suffix
    }
}