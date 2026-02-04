package com.example.domain.usecase

import com.example.domain.entity.BusinessModel
import com.example.domain.repository.BusinessRepository
import javax.inject.Inject

/**
 * Saves the full business catalog in unlock order.
 * Domain owns the contract; data provides the implementation.
 */
class SaveAllBusinessesUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(businesses: List<BusinessModel>) {
        repository.saveAllBusinesses(businesses)
    }
}