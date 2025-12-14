package com.example.domain.usecase

import com.example.domain.entity.BusinessModel
import com.example.domain.main.BusinessRepository
import javax.inject.Inject


/**
 * Returns the full business catalog in unlock order.
 * Domain owns the contract; data provides the implementation.
 */
class GetAllBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(): List<BusinessModel> {
        return repository.getAllBusinesses()
    }
}