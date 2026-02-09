package com.example.capital.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.capital.ui.state.MainUiState
import com.example.domain.entity.LevelMultiplier
import com.example.capital.R
import com.example.capital.settings.SettingsScreen
import com.example.capital.shop.ShopScreen
import com.example.capital.task.TasksScreen

sealed class Screen(val route: String, val label: String, val iconRes: Int) {
    object Adventure : Screen("adventure", "Adventure", R.drawable.baseline_add_business_24)
    object Tasks : Screen("tasks", "Tasks", R.drawable.baseline_task_24)
    object Shop : Screen("shops", "Shops", R.drawable.outline_add_shopping_cart_24)
    object Settings : Screen("settings", "Settings", R.drawable.baseline_account_balance_wallet_24)
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
        Screen.Tasks,
        Screen.Shop,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Adventure.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Adventure.route) {
                    AdventureScreen(
                        state = state, 
                        onUpgradeBusiness = onUpgradeBusiness,
                        onOpenPrestige = onOpenPrestige,
                        onClaimOffline = onClaimOffline,
                        onActivateBoost = onActivateBoost,
                        onSpeedSelected = onSpeedSelected
                    )
                }
                composable(Screen.Tasks.route) {
                    TasksScreen()
                }
                composable(Screen.Shop.route) {
                    ShopScreen()
                }
                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}
