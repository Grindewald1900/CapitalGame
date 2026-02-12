package com.yistudio.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.yistudio.data.storage.EconomyKeys
import com.yistudio.data.storage.economyDataStore
import com.yistudio.domain.entity.EconomyModel
import com.yistudio.domain.repository.EconomyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class EconomyRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val appContext: Context
) : EconomyRepository {

    override suspend fun getEconomy(): Flow<EconomyModel> =
        appContext.economyDataStore.data
            .catch { e ->
                if (e is IOException) emit(emptyPreferences()) else throw e
            }
            .map { prefs ->
                val cash = prefs[EconomyKeys.CASH] ?: 100.0
                val offline = prefs[EconomyKeys.OFFLINE_EARNINGS] ?: 0.0
                val influence = prefs[EconomyKeys.INFLUENCE] ?: 0.0
                val equity = prefs[EconomyKeys.EQUITY] ?: 0.0
                val totalCashEarned = prefs[EconomyKeys.TOTAL_CASH_EARNED] ?: 0.0
                
                EconomyModel(
                    cash = cash, 
                    offlineEarnings = offline,
                    influence = influence,
                    equity = equity,
                    totalCashEarned = totalCashEarned
                )
            }

    override suspend fun saveEconomy(model: EconomyModel) {
        appContext.economyDataStore.edit { prefs ->
            prefs[EconomyKeys.CASH] = model.cash
            prefs[EconomyKeys.OFFLINE_EARNINGS] = model.offlineEarnings
            prefs[EconomyKeys.INFLUENCE] = model.influence
            prefs[EconomyKeys.EQUITY] = model.equity
            prefs[EconomyKeys.TOTAL_CASH_EARNED] = model.totalCashEarned
        }
    }

    override suspend fun markAppBackground(nowMs: Long, incomePerSec: Double) {
        appContext.economyDataStore.edit { prefs ->
            prefs[EconomyKeys.LAST_SEEN_MS] = nowMs
            prefs[EconomyKeys.INCOME_PER_SEC] = incomePerSec
        }
    }

    override suspend fun markAppForeground(nowMs: Long, maxOfflineSeconds: Long) {
        appContext.economyDataStore.edit { prefs ->
            val lastSeen = prefs[EconomyKeys.LAST_SEEN_MS] ?: run {
                // First run: record and do nothing
                prefs[EconomyKeys.LAST_SEEN_MS] = nowMs
                return@edit
            }

            val incomePerSec = prefs[EconomyKeys.INCOME_PER_SEC] ?: 0.0
            val deltaSec = ((nowMs - lastSeen) / 1000L).coerceAtLeast(0L)
            val cappedSec = deltaSec.coerceAtMost(maxOfflineSeconds)

            val earned = incomePerSec * cappedSec.toDouble()
            if (earned > 0.0) {
                // store claimable reward; DO NOT auto-add to cash
                val existing = prefs[EconomyKeys.OFFLINE_EARNINGS] ?: 0.0
                prefs[EconomyKeys.OFFLINE_EARNINGS] = existing + earned
            }

            // reset lastSeen so we don't double-count
            prefs[EconomyKeys.LAST_SEEN_MS] = nowMs
        }
    }

    suspend fun updateCash(cash: Double) {
        appContext.economyDataStore.edit { prefs ->
            prefs[EconomyKeys.CASH] = cash
        }
    }

    suspend fun updateOfflineEarnings(value: Double?) {
        appContext.economyDataStore.edit { prefs ->
            if (value == null) prefs.remove(EconomyKeys.OFFLINE_EARNINGS)
            else prefs[EconomyKeys.OFFLINE_EARNINGS] = value
        }
    }

    suspend fun clear() {
        appContext.economyDataStore.edit { it.clear() }
    }
}
