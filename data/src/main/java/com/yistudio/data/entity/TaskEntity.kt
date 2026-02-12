package com.yistudio.data.entity

import kotlinx.serialization.Serializable

/**
 * 任务实体类：代表“战略路线图”中的一个具体业务目标。
 * 适配硅谷创投风格 UI。
 */
@Serializable
data class TaskEntity(
    /** 任务唯一标识符（例如："task_lemonade_500"） */
    val id: String,

    /** 任务标题：显示在卡片顶部的核心动词（例如："MARKET DISRUPTOR"） */
    val title: String,

    /** 任务描述：具体的执行细节（例如："Scale Lemonade Stand to Level 500"） */
    val description: String,

    /** 任务分类：SPRINT (短期), MILESTONE (中期), MOONSHOT (长期/高难) */
    val type: TaskType,

    /** 触发/监听类型：告知 Engine 该任务关注哪种游戏行为（点击、资产、等级等） */
    val triggerType: TaskTriggerType,

    /** 目标对象 ID：如果任务关联特定业务（如：柠檬水摊），存其 ID；否则为空 */
    val targetId: String? = null,

    /** 当前进度：使用 Double 以应对 Idle 游戏极高的数值膨胀 */
    val currentProgress: Double = 0.0,

    /** 目标数值：达成任务所需的最终 Double 指标 */
    val targetGoal: Double,

    /** 奖励 - 股权 (Equity)：充值类硬通货奖励 */
    val rewardEquity: Int = 0,

    /** 奖励 - 影响力 (Influence)：转生/声望类货币奖励 */
    val rewardInfluence: Double = 0.0,

    /** 奖励 - 现金 (Cash)：直接发放的资产奖励 */
    val rewardCash: Double = 0.0,

    /** 解锁门槛：要求的融资轮次（Prestige 级别），例如 Series B 才能可见 */
    val requiredSeries: Int = 0,

    /** 任务生命周期状态：LOCKED, ACTIVE, COMPLETED, CLAIMED */
    val status: TaskStatus = TaskStatus.LOCKED,

    /** 完成时间戳：用于记录任务达成时刻，便于处理每日任务重置逻辑 */
    val completedAt: Long? = null
)

@Serializable
enum class TaskTriggerType {
    ACCUMULATE_CAPITAL, // 累积资产
    BUSINESS_LEVEL,     // 业务等级
    TOTAL_CLICKS,       // 总点击
    COLLECT_INFLUENCE,  // 收集影响力
    UPGRADE_COUNT       // 升级次数
}

@Serializable
enum class TaskStatus { LOCKED, ACTIVE, COMPLETED, CLAIMED }

@Serializable
enum class TaskType { SPRINT, MILESTONE, MOONSHOT }

@Serializable
data class TaskPreferences(
    val tasks: List<TaskEntity> = emptyList(),
    val lastRefreshTimestamp: Long = 0L
)