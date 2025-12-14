package com.example.capital.ui.state

data class MainUiState(
    val cash: Double,
    val incomePerSec: Double,
    val prestigePoints: Int,
    val businesses: List<BusinessUiState>,
    val boostActive: Boolean,
    val boostSecondsLeft: Int,
    val offlineEarnings: Double?, // null = none available
) {
    companion object{
        val Empty: MainUiState = MainUiState(
            cash = 0.0,
            incomePerSec = 0.0,
            prestigePoints = 0,
            businesses = emptyList(),
            boostActive = false,
            boostSecondsLeft = 0,
            offlineEarnings = null
        )
    }
}