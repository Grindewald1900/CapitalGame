package com.example.domain.entity

import kotlin.math.pow

/**
 * Business model that drives the UI state.
 * This is the "truth" about how a business works.
 */
data class BusinessModel(
    val id: String,
    val name: String,
    val level: Int,
    val baseIncomePerSec: Double,  // income/sec at level 1 before multipliers
    val automated: Boolean,
) {
    /**
     * Example scaling rule:
     * income/sec = base * level ^ 1.15
     */
    fun incomePerSecond(multiplier: Double): Double {
        val scaled = baseIncomePerSec * level.toDouble().pow(1.15)
        return scaled * multiplier
    }

    /**
     * Upgrade cost curve:
     * cost ~ base * (level+1)^1.25 * 10
     * tweak this for pacing
     */
    fun upgradeCost(): Double {
        return baseIncomePerSec * (level + 1).toDouble().pow(1.25) * 10.0
    }

    fun levelUp(): BusinessModel = copy(level = level + 1)
}