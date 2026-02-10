package com.yistudio.capital.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yistudio.capital.ui.state.TaskUiState
import com.yistudio.data.entity.TaskType
import com.yistudio.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // 将任务列表按类型分组并映射到 UI 状态
    val uiState: StateFlow<TaskUiState> = repository.allTasks
        .map { tasks ->
            TaskUiState(
                isLoading = false,
                sprintTasks = tasks.filter { it.type == TaskType.SPRINT },
                milestoneTasks = tasks.filter { it.type == TaskType.MILESTONE },
                moonshotTasks = tasks.filter { it.type == TaskType.MOONSHOT }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TaskUiState()
        )

    /**
     * 用户点击“领取奖励”
     */
    fun onClaimReward(taskId: String) {
        viewModelScope.launch {
            repository.claimReward(taskId)
            // 这里可以触发一个侧发效应（Side Effect），比如播放一个金币掉落动画
        }
    }
}