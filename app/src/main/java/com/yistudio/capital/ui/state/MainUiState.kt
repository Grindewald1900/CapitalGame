package com.yistudio.capital.ui.state

import com.yistudio.domain.entity.LevelMultiplier

data class MainUiState(
    val cash: Double,
    val incomePerSec: Double,
    val influence: Double,
    val equity: Double,
    val prestigePoints: Int,
    val businesses: List<BusinessUiState>,
    val boostActive: Boolean,
    val boostSecondsLeft: Int,
    val offlineEarnings: Double?, // null = none available
    val selectedMultiplier: LevelMultiplier
) {
    companion object{
        val Empty: MainUiState = MainUiState(
            cash = 0.0,
            incomePerSec = 0.0,
            influence = 0.0,
            equity = 0.0,
            prestigePoints = 0,
            businesses = emptyList(),
            boostActive = false,
            boostSecondsLeft = 0,
            offlineEarnings = null,
            selectedMultiplier = LevelMultiplier.One
        )
    }
}
