package com.example.capital.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.capital.ui.state.BusinessUiState
import com.example.capital.ui.state.MainUiState

@Composable
fun MainScreen(
    state: MainUiState,
    onUpgradeBusiness: (businessId: String) -> Unit,
    onClaimOffline: () -> Unit,
    onActivateBoost: () -> Unit,
    onOpenPrestige: () -> Unit,
) {
    // Structure:
    // Column
    //   - TopHeader (cash / income / prestige)
    //   - BusinessList
    //   - BottomBar (boost / offline)
    //   - FloatingBoostTimer (if active)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 88.dp) // leave room for bottom bar
        ) {
            TopHeader(
                cash = state.cash,
                incomePerSec = state.incomePerSec,
                prestigePoints = state.prestigePoints,
                onOpenPrestige = onOpenPrestige
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


// ---------- Preview ----------

@Preview(showBackground = true, backgroundColor = 0xFF101014)
@Composable
private fun MainScreenPreview() {
    val previewState = MainUiState(
        cash = 12_450_000.0,
        incomePerSec = 24_500.0,
        prestigePoints = 3,
        businesses = listOf(
            BusinessUiState(
                id = "coffee",
                name = "Coffee Shop",
                level = 42,
                incomePerSec = 3200.0,
                upgradeCost = 15_700.0,
                automated = true
            ),
            BusinessUiState(
                id = "logistics",
                name = "Logistics Fleet",
                level = 15,
                incomePerSec = 5400.0,
                upgradeCost = 40_000.0,
                automated = false
            ),
            BusinessUiState(
                id = "startup",
                name = "AI Startup",
                level = 3,
                incomePerSec = 125_000.0,
                upgradeCost = 900_000.0,
                automated = true
            )
        ),
        boostActive = true,
        boostSecondsLeft = 18,
        offlineEarnings = 2_340_000.0
    )

    MaterialTheme(colorScheme = darkColorScheme()) {
        MainScreen(
            state = previewState,
            onUpgradeBusiness = {},
            onClaimOffline = {},
            onActivateBoost = {},
            onOpenPrestige = {}
        )
    }
}