package com.example.domain.repository

import com.example.domain.entity.EconomyModel
import kotlinx.coroutines.flow.Flow

interface EconomyRepository {
    suspend fun getEconomy(): Flow<EconomyModel>
    suspend fun saveEconomy(model: EconomyModel)
    suspend fun markAppBackground(nowMs: Long, incomePerSec: Double)
    suspend fun markAppForeground(nowMs: Long, maxOfflineSeconds: Long = 8 * 60 * 60)
}