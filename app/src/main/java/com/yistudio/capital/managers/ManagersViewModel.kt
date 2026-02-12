package com.yistudio.capital.managers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yistudio.domain.entity.ManagerEntity
import com.yistudio.domain.usecase.GetBoardStateUseCase
import com.yistudio.domain.usecase.GetEconomyUseCase
import com.yistudio.domain.usecase.GetMarketplaceUseCase
import com.yistudio.domain.usecase.RecruitManagerUseCase
import com.yistudio.domain.usecase.SaveEconomyUseCase
import com.yistudio.domain.usecase.UpdateBoardOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ManagersUiState(
    val activeManagers: List<ManagerEntity> = emptyList(),
    val marketplace: List<ManagerEntity> = emptyList(),
    val totalSlots: Int = 3,
    val totalYield: Double = 1.0,
    val luckTriggered: Boolean = false,
    val cash: Double = 0.0,
    val equity: Double = 0.0
)

@HiltViewModel
class ManagersViewModel @Inject constructor(
    private val getBoardStateUseCase: GetBoardStateUseCase,
    private val getMarketplaceUseCase: GetMarketplaceUseCase,
    private val recruitManagerUseCase: RecruitManagerUseCase,
    private val updateBoardOrderUseCase: UpdateBoardOrderUseCase,
    private val getEconomyUseCase: GetEconomyUseCase,
    private val saveEconomyUseCase: SaveEconomyUseCase
) : ViewModel() {

    private val _luckState = MutableStateFlow(false)
    private val _uiState = MutableStateFlow(ManagersUiState())
    val uiState: StateFlow<ManagersUiState> = _uiState.asStateFlow()

    init {
        initializeData()
        startLuckLoop()
    }

    private fun initializeData() {
        viewModelScope.launch {
            val boardFlow = getBoardStateUseCase()
            val marketFlow = getMarketplaceUseCase()
            
            combine(
                boardFlow,
                marketFlow,
                getEconomyUseCase(),
                _luckState
            ) { board, market, economy, luck ->
                val yield = calculateTotalYield(board.activeManagers)
                ManagersUiState(
                    activeManagers = board.activeManagers,
                    marketplace = market,
                    totalSlots = board.slots,
                    totalYield = yield,
                    luckTriggered = luck,
                    cash = economy.cash,
                    equity = economy.equity
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    private fun calculateTotalYield(managers: List<ManagerEntity>): Double {
        var multiplier = 1.0
        // Fold logic: Iterate left to right
        for (i in managers.indices) {
            val current = managers[i]
            var currentMult = current.baseMultiplier
            
            // Check synergy with previous (left neighbor)
            if (i > 0) {
                val prev = managers[i - 1]
                if (prev.industry == current.industry) {
                    currentMult *= current.synergyMultiplier
                }
            }
            multiplier *= currentMult
        }
        return multiplier
    }

    fun recruit(manager: ManagerEntity) {
        viewModelScope.launch {
            val economy = getEconomyUseCase().first()
            val canAfford = if (manager.costType == "CASH") economy.cash >= manager.costAmount else economy.equity >= manager.costAmount
            
            if (canAfford) {
                // Deduct cost
                val newEconomy = if (manager.costType == "CASH") {
                    economy.copy(cash = economy.cash - manager.costAmount)
                } else {
                    economy.copy(equity = economy.equity - manager.costAmount)
                }
                saveEconomyUseCase(newEconomy)
                
                // Recruit
                recruitManagerUseCase(manager)
            }
        }
    }

    fun swapManagers(fromIndex: Int, toIndex: Int) {
        viewModelScope.launch {
            val currentList = uiState.value.activeManagers.toMutableList()
            if (fromIndex in currentList.indices && toIndex in currentList.indices) {
                val temp = currentList[fromIndex]
                currentList[fromIndex] = currentList[toIndex]
                currentList[toIndex] = temp
                updateBoardOrderUseCase(currentList)
            }
        }
    }

    private fun startLuckLoop() {
        viewModelScope.launch {
            while (true) {
                delay(5000) // Every 5 seconds check for luck
                val managers = uiState.value.activeManagers
                var luckStruck = false
                managers.forEach { mgr ->
                    if (Math.random() < mgr.luckChance) {
                        luckStruck = true
                    }
                }
                if (luckStruck) {
                    _luckState.emit(true)
                    delay(1000) // Pulse duration
                    _luckState.emit(false)
                }
            }
        }
    }
}
