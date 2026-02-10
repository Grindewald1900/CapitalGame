package com.yistudio.domain.usecase

import com.yistudio.domain.entity.BusinessModel
import com.yistudio.domain.repository.BusinessRepository
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