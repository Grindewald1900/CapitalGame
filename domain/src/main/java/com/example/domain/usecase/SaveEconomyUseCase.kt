package com.example.domain.usecase

import com.example.domain.entity.EconomyModel
import com.example.domain.repository.EconomyRepository
import javax.inject.Inject

class SaveEconomyUseCase @Inject constructor(private val repository: EconomyRepository) {
    suspend operator fun invoke(economyModel: EconomyModel){
        repository.saveEconomy(economyModel)
    }
}