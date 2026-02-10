package com.yistudio.capital.engine.event

sealed class GameEvent {
    // 资产变动事件（用于触发资产规模任务）
    data class CapitalChanged(val totalCapital: Long) : GameEvent()

    // 业务升级事件（用于触发等级任务）
    data class BusinessUpgraded(val businessId: String, val newLevel: Int) : GameEvent()

    // 点击事件（用于触发累计点击任务）
    object ManualClick : GameEvent()

    // 收益变动事件（用于触发秒收益任务）
    data class IncomeChanged(val incomePerSecond: Long) : GameEvent()
}