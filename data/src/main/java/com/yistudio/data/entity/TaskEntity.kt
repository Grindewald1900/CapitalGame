package com.yistudio.data.entity

import kotlinx.serialization.Serializable

@Serializable
enum class TaskStatus { LOCKED, ACTIVE, COMPLETED, CLAIMED }

@Serializable
enum class TaskType { SPRINT, MILESTONE, MOONSHOT }

@Serializable
data class TaskEntity(
    val id: String,
    val title: String,
    val description: String,
    val type: TaskType,
    val currentProgress: Long = 0L,
    val targetGoal: Long,
    val rewardEquity: Int = 0,    // 对应 image_69293e.png 中的 Equity
    val rewardInfluence: Long = 0L, // 对应 image_69293e.png 中的 Influence
    val status: TaskStatus = TaskStatus.LOCKED
)

@Serializable
data class TaskPreferences(
    val tasks: List<TaskEntity> = emptyList(),
    val lastRefreshTimestamp: Long = 0L
)