package com.example.domain.repository

import com.example.domain.entity.BusinessModel
import kotlinx.coroutines.flow.Flow

interface BusinessRepository {
    /**
     * Ordered list of business definitions.
     * Order controls unlock sequence (0, 1, 2, ...)
     */
    fun getAllBusinesses(): Flow<List<BusinessModel>>
    suspend fun saveAllBusinesses(businesses: List<BusinessModel>)
}