package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.capital.ui.state.MainUiState

@Composable
fun AdventureScreen(
    state: MainUiState,
    onUpgradeBusiness: (businessId: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BusinessList(
            businesses = state.businesses,
            onUpgrade = onUpgradeBusiness
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
