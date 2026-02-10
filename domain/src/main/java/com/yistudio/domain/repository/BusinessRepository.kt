package com.yistudio.domain.repository

import com.yistudio.domain.entity.BusinessModel
import kotlinx.coroutines.flow.Flow

interface BusinessRepository {
    /**
     * Ordered list of business definitions.
     * Order controls unlock sequence (0, 1, 2, ...)
     */
    fun getAllBusinesses(): Flow<List<BusinessModel>>
    suspend fun saveAllBusinesses(businesses: List<BusinessModel>)
}