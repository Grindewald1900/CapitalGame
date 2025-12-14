package com.example.capital.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.capital.model.LevelMultiplier

object Routes {
    const val MAIN = "main"
}

@Composable
fun MainNavGraph() {
    val navigationController = rememberNavController()

    NavHost(navController = navigationController, startDestination = Routes.MAIN) {
        composable(Routes.MAIN){
            val viewModel: MainViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsState()
            MainScreen(
                state.value,
                onUpgradeBusiness = { id -> viewModel.onUpgradeBusiness(id) },
                onClaimOffline = { viewModel.onClaimOffline() },
                onActivateBoost = { viewModel.onActivateBoost() },
                onOpenPrestige = { viewModel.onOpenPrestige() },
                onSpeedSelected = { viewModel.onLevelMultiplierSelected(LevelMultiplier.One())}
            )
        }
    }

}