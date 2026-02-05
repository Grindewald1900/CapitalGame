package com.example.capital.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.entity.LevelMultiplier
import com.example.capital.ui.state.BusinessUiState
import com.example.capital.ui.state.MainUiState

@Composable
fun MainScreen(
    state: MainUiState,
    onUpgradeBusiness: (businessId: String) -> Unit,
    onClaimOffline: () -> Unit,
    onActivateBoost: () -> Unit,
    onOpenPrestige: () -> Unit,
    onSpeedSelected: (speed: LevelMultiplier) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 88.dp)
        ) {
            TopHeader(
                cash = state.cash,
                incomePerSec = state.incomePerSec,
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

        BottomActionBar(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            offlineEarnings = state.offlineEarnings,
            onClaimOffline = onClaimOffline,
            onActivateBoost = onActivateBoost
        )

        if (state.boostActive) {
            BoostCountdownChip(
                secondsLeft = state.boostSecondsLeft,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 96.dp, end = 16.dp)
            )
        }
    }
}
