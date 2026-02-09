package com.example.capital.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.BusinessModel
import com.example.capital.model.GlobalModifiers
import com.example.capital.ui.state.BusinessUiState
import com.example.capital.ui.state.GameState
import com.example.capital.ui.state.MainUiState
import com.example.domain.entity.EconomyModel
import com.example.domain.entity.LevelMultiplier
import com.example.domain.usecase.GetAllBusinessUseCase
import com.example.domain.usecase.GetEconomyUseCase
import com.example.domain.usecase.SaveAllBusinessesUseCase
import com.example.domain.usecase.SaveEconomyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAlBusinessesUseCase: GetAllBusinessUseCase,
    private val getEconomyUseCase: GetEconomyUseCase,
    private val saveEconomyUseCase: SaveEconomyUseCase,
    private val saveAllBusinessesUseCase: SaveAllBusinessesUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState.Empty)
    val uiState: StateFlow<MainUiState> = _uiState

    private var gameState: GameState = GameState(
        businesses = emptyList(),
        modifiers = GlobalModifiers.Default,
        economy = EconomyModel(cash = 100.0, offlineEarnings = 0.0)
    )

    private var selectedMultiplier: LevelMultiplier = LevelMultiplier.One

    private var tickerJob: Job? = null

    init {
        startTicker()
        loadBusinesses()
    }

    fun onUpgradeBusiness(businessId: String) {
        val gs = gameState
        val bizIndex = gs.businesses.indexOfFirst { it.id == businessId }
        if (bizIndex == -1) return

        val biz = gs.businesses[bizIndex]
        val (cost, levels) = biz.calculateBulkUpgrade(selectedMultiplier, gs.economy.cash)

        if (levels <= 0 || gs.economy.cash < cost) return

        val upgraded = biz.levelUp(levels)
        val newBizList = gs.businesses.toMutableList().apply {
            this[bizIndex] = upgraded
        }

        viewModelScope.launch {
            saveAllBusinessesUseCase(newBizList)
        }

        gameState = gs.copy(
            businesses = newBizList,
            economy = gs.economy.copy(
                cash = gs.economy.cash - cost
            )
        )

        pushToUi()
    }

    fun onClaimOffline() {
        val gs = gameState
        val reward = gs.economy.offlineEarnings ?: 0.0
        if (reward <= 0.0) return

        val economy = gs.economy.copy(
            cash = gs.economy.cash + reward,
            offlineEarnings = 0.0
        )
        gameState = gs.copy(
            economy = economy
        )
        viewModelScope.launch {
            saveEconomyUseCase(economy)
        }

        pushToUi()
    }

    fun onActivateBoost() {
        val gs = gameState
        if (gs.modifiers.boostActive) return
        gameState = gs.copy(
            modifiers = gs.modifiers.copy(
                boostActive = true,
                boostSecondsLeft = 30,
                boostMultiplier = 5.0
            )
        )
        pushToUi()
    }

    fun onOpenPrestige() {}

    fun onLevelMultiplierSelected(multiplier: LevelMultiplier) {
        selectedMultiplier = multiplier
        pushToUi()
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            var lastTickTime = System.currentTimeMillis()
            while (true) {
                delay(1000L)
                val now = System.currentTimeMillis()
                val deltaSeconds = ((now - lastTickTime) / 1000.0).coerceAtLeast(0.0)
                lastTickTime = now
                tick(deltaSeconds)
            }
        }
    }

    private fun tick(deltaSeconds: Double) {
        val gs = gameState
        val totalIncomePerSec = computeTotalIncomePerSec(gs)
        val earned = totalIncomePerSec * deltaSeconds
        val newCash = gs.economy.cash + earned

        val newBoostSecondsLeft = if (gs.modifiers.boostActive) {
            (gs.modifiers.boostSecondsLeft - deltaSeconds).toInt().coerceAtLeast(0)
        } else {
            gs.modifiers.boostSecondsLeft
        }

        val boostStillActive = gs.modifiers.boostActive && newBoostSecondsLeft > 0
        val newBoostMultiplier = if (boostStillActive) gs.modifiers.boostMultiplier else 1.0

        val updatedEconomy = gs.economy.copy(
            cash = newCash,
            offlineEarnings = gs.economy.offlineEarnings
        )
        
        gameState = gs.copy(
            economy = updatedEconomy,
            modifiers = gs.modifiers.copy(
                boostActive = boostStillActive,
                boostSecondsLeft = newBoostSecondsLeft,
                boostMultiplier = newBoostMultiplier
            )
        )

        viewModelScope.launch {
            saveEconomyUseCase(updatedEconomy)
        }

        pushToUi()
    }

    private fun loadBusinesses() {
        viewModelScope.launch {
            getAlBusinessesUseCase().collect { businesses ->
                gameState = gameState.copy(businesses = businesses)
                pushToUi()
            }
        }
        viewModelScope.launch {
            getEconomyUseCase().collect { savedEconomy ->
                gameState = gameState.copy(economy = savedEconomy)
                pushToUi()
            }
        }
        val modifiers = GlobalModifiers(
            boostActive = false,
            boostSecondsLeft = 0,
            boostMultiplier = 1.0,
            prestigePoints = gameState.modifiers.prestigePoints
        )
        gameState = gameState.copy(modifiers = modifiers)
        pushToUi()
    }

    private fun computeTotalIncomePerSec(gs: GameState): Double {
        return gs.businesses.sumOf { biz ->
            biz.incomePerSecond(gs.modifiers.boostMultiplier)
        }
    }

    private fun pushToUi() {
        _uiState.value = gameState.toUiState()
    }

    private fun GameState.toUiState(): MainUiState {
        val totalIncomePerSec = computeTotalIncomePerSec(this)
        
        val visibleBusinesses = mutableListOf<BusinessModel>()
        var foundFirstLocked = false
        for (biz in businesses) {
            if (biz.level > 0) {
                visibleBusinesses.add(biz)
            } else if (!foundFirstLocked) {
                visibleBusinesses.add(biz)
                foundFirstLocked = true
            }
        }

        return MainUiState(
            cash = economy.cash,
            incomePerSec = totalIncomePerSec,
            influence = economy.influence,
            equity = economy.equity,
            prestigePoints = modifiers.prestigePoints,
            businesses = visibleBusinesses.map { it.toUi(economy.cash) },
            boostActive = modifiers.boostActive,
            boostSecondsLeft = modifiers.boostSecondsLeft,
            offlineEarnings = economy.offlineEarnings,
            selectedMultiplier = selectedMultiplier
        )
    }

    private fun BusinessModel.toUi(currentCash: Double): BusinessUiState {
        val (cost, levels) = calculateBulkUpgrade(selectedMultiplier, currentCash)
        return BusinessUiState(
            id = id,
            name = name,
            level = level,
            incomePerSec = incomePerSecond(multiplier = 1.0),
            upgradeCost = cost,
            upgradeLevelGain = levels,
            canAfford = currentCash >= cost && levels > 0,
            automated = automated
        )
    }
}
