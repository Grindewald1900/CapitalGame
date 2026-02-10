package com.yistudio.capital.ui.state

import com.yistudio.domain.entity.EconomyModel

/**
 * Persistent player economy snapshot.
 */
sealed interface EconomyState {
    data object Loading : EconomyState
    data class Success(val model: EconomyModel) : EconomyState
    data class Fail(val message: String, val cause: Throwable? = null) : EconomyState
}