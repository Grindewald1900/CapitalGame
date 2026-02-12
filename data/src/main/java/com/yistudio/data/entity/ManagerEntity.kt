package com.yistudio.data.entity

import kotlinx.serialization.Serializable

@Serializable
enum class IndustryTag {
    TECH, FINANCE, FOOD, ENERGY, RETAIL
}

@Serializable
enum class ManagerRarity {
    COMMON, RARE, LEGENDARY
}

@Serializable
data class ManagerEntity(
    val id: String,
    val name: String,
    val title: String, // e.g., "Visionary CTO"
    val industry: IndustryTag,
    val rarity: ManagerRarity,
    val baseMultiplier: Double,
    val synergyMultiplier: Double, // Bonus if adjacent to same industry
    val costType: String, // "CASH" or "EQUITY"
    val costAmount: Double,
    val luckChance: Double, // 0.0 to 1.0 (e.g. 0.25 for 25%)
    val burnRateSeconds: Int? = null, // If null, no burn rate. If set, needs maintenance.
    val portraitResId: Int? = null // Placeholder for image resource
)

@Serializable
data class BoardState(
    val slots: Int = 3,
    val activeManagers: List<ManagerEntity> = emptyList()
)
