package com.yistudio.domain.usecase

import com.yistudio.domain.entity.EconomyModel
import com.yistudio.domain.repository.EconomyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEconomyUseCase @Inject constructor(private val repository: EconomyRepository) {
    suspend operator fun invoke(): Flow<EconomyModel> {
        return repository.getEconomy()
    }
}