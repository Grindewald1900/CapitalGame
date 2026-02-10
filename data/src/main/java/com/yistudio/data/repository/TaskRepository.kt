package com.yistudio.data.repository

import androidx.datastore.core.DataStore
import com.yistudio.data.entity.TaskEntity
import com.yistudio.data.entity.TaskPreferences
import com.yistudio.data.entity.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDataStore: DataStore<TaskPreferences>
) {
    // 观察所有任务流，用于 Compose UI 实时更新
    val allTasks: Flow<List<TaskEntity>> = taskDataStore.data.map { it.tasks }

    suspend fun updateTaskProgress(taskId: String, progress: Long) {
        taskDataStore.updateData { currentPrefs ->
            val updatedTasks = currentPrefs.tasks.map { task ->
                if (task.id == taskId && task.status == TaskStatus.ACTIVE) {
                    val newProgress = (task.currentProgress + progress).coerceAtMost(task.targetGoal)
                    task.copy(
                        currentProgress = newProgress,
                        status = if (newProgress >= task.targetGoal) TaskStatus.COMPLETED else TaskStatus.ACTIVE
                    )
                } else task
            }
            currentPrefs.copy(tasks = updatedTasks)
        }
    }

    suspend fun claimReward(taskId: String) {
        taskDataStore.updateData { currentPrefs ->
            currentPrefs.copy(tasks = currentPrefs.tasks.map {
                if (it.id == taskId) it.copy(status = TaskStatus.CLAIMED) else it
            })
        }
    }

    suspend fun syncTasksFromServer(newTasks: List<TaskEntity>) {
        taskDataStore.updateData { it.copy(tasks = newTasks) }
    }
}