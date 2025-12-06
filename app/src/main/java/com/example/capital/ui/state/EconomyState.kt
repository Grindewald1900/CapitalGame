package com.example.capital.ui.state

/**
 * Persistent player economy snapshot.
 */
data class EconomyState(
    val cash: Double,
    val offlineEarnings: Double?,     // what's claimable
)