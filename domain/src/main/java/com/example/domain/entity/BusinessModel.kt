package com.example.domain.entity

import kotlin.math.pow

data class BusinessModel(
    val id: String,
    val name: String,
    val level: Int,
    val baseIncomePerSec: Double,
    val automated: Boolean,
) {
    fun incomePerSecond(multiplier: Double): Double {
        if (level == 0) return 0.0
        val scaled = baseIncomePerSec * level.toDouble().pow(1.15)
        return scaled * multiplier
    }

    /**
     * Calculates total cost for one level
     */
    fun upgradeCost(): Double = calculateCostForLevel(level)

    private fun calculateCostForLevel(lvl: Int): Double {
        return baseIncomePerSec * (lvl + 1).toDouble().pow(1.25) * 10.0
    }

    /**
     * Returns (totalCost, levelsToGain)
     */
    fun calculateBulkUpgrade(multiplier: LevelMultiplier, currentCash: Double): Pair<Double, Int> {
        return when (multiplier) {
            is LevelMultiplier.One -> upgradeCost() to 1
            is LevelMultiplier.Ten -> calculateBulk(10)
            is LevelMultiplier.TwentyFive -> calculateBulk(25)
            is LevelMultiplier.Hundred -> calculateBulk(100)
            is LevelMultiplier.Max -> calculateMax(currentCash)
        }
    }

    private fun calculateBulk(count: Int): Pair<Double, Int> {
        var total = 0.0
        for (i in 0 until count) {
            total += calculateCostForLevel(level + i)
        }
        return total to count
    }

    private fun calculateMax(cash: Double): Pair<Double, Int> {
        var total = 0.0
        var count = 0
        // If locked, first level is mandatory
        if (level == 0) {
            val cost = upgradeCost()
            return if (cash >= cost) cost to 1 else 0.0 to 0
        }
        
        while (true) {
            val nextCost = calculateCostForLevel(level + count)
            if (total + nextCost <= cash) {
                total += nextCost
                count++
            } else {
                break
            }
        }
        return total to (if (count == 0 && cash >= upgradeCost()) 1 else count)
    }

    fun levelUp(count: Int = 1): BusinessModel = copy(level = level + count)
}
