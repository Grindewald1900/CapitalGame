package com.example.capital.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.BusinessModel
import com.example.capital.model.GlobalModifiers
import com.example.capital.model.LevelMultiplier
import com.example.capital.ui.state.BusinessUiState
import com.example.capital.ui.state.EconomyState
import com.example.capital.ui.state.GameState
import com.example.capital.ui.state.MainUiState
import com.example.domain.usecase.GetAllBusinessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.indexOfFirst
import kotlin.collections.toMutableList

@HiltViewModel
class MainViewModel @Inject constructor(private val getAlBusinessesUseCase: GetAllBusinessUseCase) :
    ViewModel() {

    // ---- StateFlow exposed to UI ----
    private val _uiState = MutableStateFlow(MainUiState.Empty)
    val uiState: StateFlow<MainUiState> = _uiState

    // ---- Internal mutable game state (not exposed directly) ----
    private lateinit var gameState: GameState

    private var tickerJob: Job? = null

    init {
        startTicker()
        loadBusinesses()
    }

    // ------------------------------------------------------------
    // Public callbacks (these match MainScreen callbacks)
    // ------------------------------------------------------------

    fun onUpgradeBusiness(businessId: String) {
        val gs = gameState
        val bizIndex = gs.businesses.indexOfFirst { it.id == businessId }
        if (bizIndex == -1) return

        val biz = gs.businesses[bizIndex]
        val cost = biz.upgradeCost()

        // can we afford it?
        if (gs.economy.cash < cost) return

        // pay & level up
        val upgraded = biz.levelUp()
        val newBizList = gs.businesses.toMutableList().apply {
            this[bizIndex] = upgraded
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

        gameState = gs.copy(
            economy = gs.economy.copy(
                cash = gs.economy.cash + reward,
                offlineEarnings = null
            )
        )

        pushToUi()
    }

    fun onActivateBoost() {
        val gs = gameState

        // If boost already running, do nothing.
        if (gs.modifiers.boostActive) return

        // Activate a 30s 5x boost.
        gameState = gs.copy(
            modifiers = gs.modifiers.copy(
                boostActive = true,
                boostSecondsLeft = 30,
                boostMultiplier = 5.0
            )
        )

        pushToUi()
    }

    fun onOpenPrestige() {
        // In a real game this would navigate to Prestige screen
        // For now we could do nothing or log.
        // We'll just no-op here.
    }

    fun onLevelMultiplierSelected(multiplier: LevelMultiplier) {

    }

    // ------------------------------------------------------------
    // Core loop: runs every second
    // ------------------------------------------------------------

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

    /**
     * The heartbeat of the game.
     * - Adds passive income
     * - Burns down boost duration
     * - Accumulates offline earnings logic stub
     */
    private fun tick(deltaSeconds: Double) {
        val gs = gameState

        // 1. Calculate total income/sec with current multipliers
        val totalIncomePerSec = computeTotalIncomePerSec(gs)

        // 2. Earn money for this tick
        val earned = totalIncomePerSec * deltaSeconds
        val newCash = gs.economy.cash + earned

        // 3. Update boost timer
        val newBoostSecondsLeft = if (gs.modifiers.boostActive) {
            (gs.modifiers.boostSecondsLeft - deltaSeconds).toInt().coerceAtLeast(0)
        } else {
            gs.modifiers.boostSecondsLeft
        }

        val boostStillActive = gs.modifiers.boostActive && newBoostSecondsLeft > 0

        val newBoostMultiplier =
            if (boostStillActive) gs.modifiers.boostMultiplier else 1.0

        // 4. Offline earnings accumulation (stub logic)
        // In a real app:
        // - when app backgrounds, snapshot timestamp
        // - on resume, compute delta and store in offlineEarnings
        // Here we'll just keep whatever was there.
        val offlineStaySame = gs.economy.offlineEarnings

        // 5. Save new state
        gameState = gs.copy(
            economy = gs.economy.copy(
                cash = newCash,
                offlineEarnings = offlineStaySame
            ),
            modifiers = gs.modifiers.copy(
                boostActive = boostStillActive,
                boostSecondsLeft = newBoostSecondsLeft,
                boostMultiplier = newBoostMultiplier
            )
        )

        pushToUi()
    }

    private fun loadBusinesses() {
        viewModelScope.launch {
            // 1) call use case
            val startingBusinesses = getAlBusinessesUseCase()

            val modifiers = GlobalModifiers(
                boostActive = false,
                boostSecondsLeft = 0,
                boostMultiplier = 1.0,
                prestigePoints = 0
            )

            val economy = EconomyState(
                cash = 1_000.0,
                offlineEarnings = 2_000.0, // pretend we had some offline stash to claim
            )

            gameState = GameState(
                businesses = startingBusinesses,
                modifiers = modifiers,
                economy = economy
            )
        }
    }

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------

    private fun computeTotalIncomePerSec(gs: GameState): Double {
        return gs.businesses.sumOf { biz ->
            biz.incomePerSecond(gs.modifiers.boostMultiplier)
        }
    }

    private fun pushToUi() {
        val gs = gameState
        _uiState.value = gs.toUiState()
    }

    // ------------------------------------------------------------
    // Initial State
    // ------------------------------------------------------------

    private fun createInitialUiState(): MainUiState {
        return gameState.toUiState()
    }

    // ------------------------------------------------------------
    // Mapping GameState -> MainUiState (for Compose)
    // ------------------------------------------------------------

    private fun GameState.toUiState(): MainUiState {
        val totalIncomePerSec = computeTotalIncomePerSec(this)
        return MainUiState(
            cash = economy.cash,
            incomePerSec = totalIncomePerSec,
            prestigePoints = modifiers.prestigePoints,
            businesses = businesses.map { it.toUi() },
            boostActive = modifiers.boostActive,
            boostSecondsLeft = modifiers.boostSecondsLeft,
            offlineEarnings = economy.offlineEarnings
        )
    }

    private fun BusinessModel.toUi(): BusinessUiState {
        return BusinessUiState(
            id = id,
            name = name,
            level = level,
            incomePerSec = incomePerSecond(multiplier = 1.0), // UI shows base/sec without global boost
            upgradeCost = upgradeCost(),
            automated = automated
        )
    }
}