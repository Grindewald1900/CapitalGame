package com.yistudio.data.storage

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

private const val ECONOMY_PREFS = "economy_prefs"

val Context.economyDataStore: DataStore<Preferences> by preferencesDataStore(
    name = ECONOMY_PREFS
)

object EconomyKeys {
    val CASH = doublePreferencesKey("cash")
    val OFFLINE_EARNINGS = doublePreferencesKey("offline_earnings") // store only when non-null
    val LAST_SEEN_MS = longPreferencesKey("last_seen_ms")
    val INCOME_PER_SEC = doublePreferencesKey("income_per_sec_at_exit")
}
