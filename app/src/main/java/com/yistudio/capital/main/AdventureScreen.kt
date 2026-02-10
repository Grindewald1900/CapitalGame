package com.yistudio.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yistudio.capital.ui.state.MainUiState
import com.yistudio.domain.entity.LevelMultiplier

@Composable
fun AdventureScreen(
    state: MainUiState,
    onUpgradeBusiness: (businessId: String) -> Unit,
    onOpenPrestige: () -> Unit,
    onClaimOffline: () -> Unit,
    onActivateBoost: () -> Unit,
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
                onSpeedSelected = onSpeedSelected,
            )
            BusinessList(
                businesses = state.businesses,
                onUpgrade = onUpgradeBusiness,
                modifier = Modifier.weight(1f) // Make BusinessList take up available space
            )
            BottomActionBar(
                offlineEarnings = state.offlineEarnings,
                onClaimOffline = onClaimOffline,
                onActivateBoost = onActivateBoost,
            )
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
