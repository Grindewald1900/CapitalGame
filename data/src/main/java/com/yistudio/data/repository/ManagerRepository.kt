package com.yistudio.data.repository

import com.yistudio.domain.entity.BoardState
import com.yistudio.domain.entity.IndustryTag
import com.yistudio.domain.entity.ManagerEntity
import com.yistudio.domain.entity.ManagerRarity
import com.yistudio.domain.repository.ManagerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagerRepositoryImpl @Inject constructor() : ManagerRepository {

    // In a real app, this would be backed by DataStore
    private val _boardState = MutableStateFlow(BoardState(slots = 5, activeManagers = emptyList()))
    override val boardState: StateFlow<BoardState> = _boardState.asStateFlow()

    // Mock Marketplace
    private val _marketplace = MutableStateFlow(generateMockMarketplace())
    override val marketplace: StateFlow<List<ManagerEntity>> = _marketplace.asStateFlow()

    override suspend fun recruitManager(manager: ManagerEntity) {
        val currentBoard = _boardState.value
        if (currentBoard.activeManagers.size < currentBoard.slots) {
            val newList = currentBoard.activeManagers + manager
            _boardState.emit(currentBoard.copy(activeManagers = newList))
            
            // Remove from marketplace
            val newMarket = _marketplace.value.filter { it.id != manager.id }
            _marketplace.emit(newMarket)
        }
    }

    override suspend fun updateBoardOrder(newOrder: List<ManagerEntity>) {
        _boardState.emit(_boardState.value.copy(activeManagers = newOrder))
    }

    override suspend fun fireManager(managerId: String) {
        val currentBoard = _boardState.value
        val newList = currentBoard.activeManagers.filter { it.id != managerId }
        _boardState.emit(currentBoard.copy(activeManagers = newList))
    }

    private fun generateMockMarketplace(): List<ManagerEntity> {
        return listOf(
            ManagerEntity(
                id = "m1", name = "Sarah Jenkins", title = "Growth Hacker",
                industry = IndustryTag.TECH, rarity = ManagerRarity.COMMON,
                baseMultiplier = 1.5, synergyMultiplier = 1.2,
                costType = "CASH", costAmount = 1000.0, luckChance = 0.1
            ),
            ManagerEntity(
                id = "m2", name = "Gordon Gekko", title = "Raider",
                industry = IndustryTag.FINANCE, rarity = ManagerRarity.LEGENDARY,
                baseMultiplier = 5.0, synergyMultiplier = 2.0,
                costType = "EQUITY", costAmount = 10.0, luckChance = 0.5,
                burnRateSeconds = 60
            ),
            ManagerEntity(
                id = "m3", name = "Chef Ramsey", title = "Head Chef",
                industry = IndustryTag.FOOD, rarity = ManagerRarity.RARE,
                baseMultiplier = 3.0, synergyMultiplier = 1.5,
                costType = "CASH", costAmount = 50000.0, luckChance = 0.2
            ),
            ManagerEntity(
                id = "m4", name = "Elon M.", title = "Visionary",
                industry = IndustryTag.TECH, rarity = ManagerRarity.LEGENDARY,
                baseMultiplier = 10.0, synergyMultiplier = 3.0,
                costType = "EQUITY", costAmount = 50.0, luckChance = 0.05
            ),
            ManagerEntity(
                id = "m5", name = "Alice Walton", title = "Retail Queen",
                industry = IndustryTag.RETAIL, rarity = ManagerRarity.COMMON,
                baseMultiplier = 2.0, synergyMultiplier = 1.1,
                costType = "CASH", costAmount = 2500.0, luckChance = 0.15
            )
        )
    }
}
