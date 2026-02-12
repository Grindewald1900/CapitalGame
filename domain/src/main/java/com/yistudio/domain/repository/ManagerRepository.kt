package com.yistudio.domain.repository

import com.yistudio.domain.entity.BoardState
import com.yistudio.domain.entity.ManagerEntity
import kotlinx.coroutines.flow.StateFlow

interface ManagerRepository {
    val boardState: StateFlow<BoardState>
    val marketplace: StateFlow<List<ManagerEntity>>

    suspend fun recruitManager(manager: ManagerEntity)
    suspend fun updateBoardOrder(newOrder: List<ManagerEntity>)
    suspend fun fireManager(managerId: String)
}
