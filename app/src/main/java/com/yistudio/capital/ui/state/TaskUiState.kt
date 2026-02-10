package com.yistudio.capital.ui.state

import com.yistudio.data.entity.TaskEntity

data class TaskUiState(
    val isLoading: Boolean = true,
    val sprintTasks: List<TaskEntity> = emptyList(),
    val milestoneTasks: List<TaskEntity> = emptyList(),
    val moonshotTasks: List<TaskEntity> = emptyList(),
    val errorMessage: String? = null
)