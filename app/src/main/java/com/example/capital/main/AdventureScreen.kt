package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.capital.ui.state.MainUiState
import com.example.domain.entity.LevelMultiplier

@Composable
fun AdventureScreen(
    state: MainUiState,
    onUpgradeBusiness: (businessId: String) -> Unit,
    onOpenPrestige: () -> Unit,
    onSpeedSelected: (LevelMultiplier) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopHeader(
                cash = state.cash,
                incomePerSec = state.incomePerSec,
                influence = state.influence,
                equity = state.equity,
                prestigePoints = state.prestigePoints,
                selectedMultiplier = state.selectedMultiplier,
                onOpenPrestige = onOpenPrestige,
                onSpeedSelected = onSpeedSelected
            )

            BusinessList(
                businesses = state.businesses,
                onUpgrade = onUpgradeBusiness
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.boostActive) {
            BoostCountdownChip(
                secondsLeft = state.boostSecondsLeft,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 120.dp, end = 16.dp)
            )
        }
    }
}
