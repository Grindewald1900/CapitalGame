package com.example.capital.ui.state

data class BusinessUiState(
    val id: String,
    val name: String,
    val level: Int,
    val incomePerSec: Double,    // raw number, e.g. 3200.0
    val upgradeCost: Double,     // cost for the NEXT upgrade(s)
    val upgradeLevelGain: Int,   // how many levels will be gained
    val canAfford: Boolean,
    val automated: Boolean,
)
