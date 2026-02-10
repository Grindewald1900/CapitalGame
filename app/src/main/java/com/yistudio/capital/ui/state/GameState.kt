package com.yistudio.capital.ui.state

import com.yistudio.domain.entity.BusinessModel
import com.yistudio.capital.model.GlobalModifiers
import com.yistudio.domain.entity.EconomyModel

/**
 * Full internal game state.
 */
data class GameState(
    val businesses: List<BusinessModel>,
    val modifiers: GlobalModifiers,
    val economy: EconomyModel,
)