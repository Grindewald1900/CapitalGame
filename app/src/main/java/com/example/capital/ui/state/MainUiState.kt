package com.example.capital.ui.state

data class MainUiState(
    val cash: Double,
    val incomePerSec: Double,
    val prestigePoints: Int,
    val businesses: List<BusinessUiState>,
    val boostActive: Boolean,
    val boostSecondsLeft: Int,
    val offlineEarnings: Double?, // null = none available
)