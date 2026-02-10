package com.yistudio.domain.usecase

import com.yistudio.domain.entity.BusinessModel
import com.yistudio.domain.repository.BusinessRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Returns the full business catalog in unlock order.
 * Domain owns the contract; data provides the implementation.
 */
class GetAllBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(): Flow<List<BusinessModel>> {
        return repository.getAllBusinesses()
    }
}