package com.example.capital.ui.state

import com.example.domain.entity.BusinessModel
import com.example.capital.model.GlobalModifiers
import com.example.domain.entity.EconomyModel

/**
 * Full internal game state.
 */
data class GameState(
    val businesses: List<BusinessModel>,
    val modifiers: GlobalModifiers,
    val economy: EconomyModel,
)