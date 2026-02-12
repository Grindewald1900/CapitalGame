package com.yistudio.domain.usecase

import com.yistudio.domain.entity.BoardState
import com.yistudio.domain.entity.ManagerEntity
import com.yistudio.domain.repository.ManagerRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetBoardStateUseCase @Inject constructor(
    private val repository: ManagerRepository
) {
    suspend operator fun invoke(): StateFlow<BoardState> = repository.boardState
}

class GetMarketplaceUseCase @Inject constructor(
    private val repository: ManagerRepository
) {
    suspend operator fun invoke(): StateFlow<List<ManagerEntity>> = repository.marketplace
}

class RecruitManagerUseCase @Inject constructor(
    private val repository: ManagerRepository
) {
    suspend operator fun invoke(manager: ManagerEntity) = repository.recruitManager(manager)
}

class UpdateBoardOrderUseCase @Inject constructor(
    private val repository: ManagerRepository
) {
    suspend operator fun invoke(newOrder: List<ManagerEntity>) = repository.updateBoardOrder(newOrder)
}
