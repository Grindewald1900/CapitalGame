package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ManagersScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Managers", style = MaterialTheme.typography.headlineMedium)
            Text("Automate your businesses here!", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Coming Soon...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun GlobalUpgradesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Upgrades", style = MaterialTheme.typography.headlineMedium)
            Text("Global multipliers and bonuses", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Coming Soon...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun InvestorsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Investors", style = MaterialTheme.typography.headlineMedium)
            Text("Prestige and earn Angel Investors", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Coming Soon...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
