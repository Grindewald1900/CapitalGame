package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.capital.ui.state.MainUiState
import com.example.domain.entity.LevelMultiplier
import com.example.capital.R

sealed class Screen(val route: String, val label: String, val iconRes: Int) {
    object Adventure : Screen("adventure", "Adventure", com.example.capital.R.drawable.baseline_add_business_24)
    object Managers : Screen("managers", "Managers", R.drawable.baseline_manage_accounts_24)
    object Upgrades : Screen("upgrades", "Upgrades", R.drawable.baseline_trending_up_24)
    object Investors : Screen("settings", "Settings", R.drawable.baseline_account_balance_wallet_24)
}

@Composable
fun MainScreen(
    state: MainUiState,
    onUpgradeBusiness: (businessId: String) -> Unit,
    onClaimOffline: () -> Unit,
    onActivateBoost: () -> Unit,
    onOpenPrestige: () -> Unit,
    onSpeedSelected: (LevelMultiplier) -> Unit
) {
    val navController = rememberNavController()
    val navItems = listOf(
        Screen.Adventure,
        Screen.Managers,
        Screen.Upgrades,
        Screen.Investors
    )

    Scaffold(
        bottomBar = {
            Column {
                BottomActionBar(
                    offlineEarnings = state.offlineEarnings,
                    onClaimOffline = onClaimOffline,
                    onActivateBoost = onActivateBoost
                )
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    painter = painterResource(id = screen.iconRes), 
                                    contentDescription = null 
                                ) 
                            },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
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

                NavHost(
                    navController = navController,
                    startDestination = Screen.Adventure.route,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(Screen.Adventure.route) {
                        AdventureScreen(state = state, onUpgradeBusiness = onUpgradeBusiness)
                    }
                    composable(Screen.Managers.route) {
                        ManagersScreen()
                    }
                    composable(Screen.Upgrades.route) {
                        GlobalUpgradesScreen()
                    }
                    composable(Screen.Investors.route) {
                        InvestorsScreen()
                    }
                }
            }

            if (state.boostActive) {
                BoostCountdownChip(
                    secondsLeft = state.boostSecondsLeft,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 120.dp, end = 16.dp) // Adjusted padding for new header height
                )
            }
        }
    }
}
