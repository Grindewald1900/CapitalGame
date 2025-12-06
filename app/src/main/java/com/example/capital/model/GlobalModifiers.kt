package com.example.capital.model


/**
 * Game-wide modifiers that aren't tied to a single business.
 * Boosts, prestige, research, etc.
 */
data class GlobalModifiers(
    val boostActive: Boolean,
    val boostSecondsLeft: Int,
    val boostMultiplier: Double,      // e.g. 5.0 during boost, 1.0 normally
    val prestigePoints: Int,
)
