package com.yistudio.domain.usecase

import com.yistudio.domain.entity.EconomyModel
import com.yistudio.domain.repository.EconomyRepository
import javax.inject.Inject

class SaveEconomyUseCase @Inject constructor(private val repository: EconomyRepository) {
    suspend operator fun invoke(economyModel: EconomyModel){
        repository.saveEconomy(economyModel)
    }
}