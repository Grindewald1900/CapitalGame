package com.example.domain.usecase

import com.example.domain.entity.EconomyModel
import com.example.domain.repository.EconomyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEconomyUseCase @Inject constructor(private val repository: EconomyRepository) {
    suspend operator fun invoke(): Flow<EconomyModel> {
        return repository.getEconomy()
    }
}