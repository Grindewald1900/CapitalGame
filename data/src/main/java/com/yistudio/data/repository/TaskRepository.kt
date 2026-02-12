package com.yistudio.data.repository

import androidx.datastore.core.DataStore
import com.yistudio.data.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDataStore: DataStore<TaskPreferences>
) {
    // 观察所有任务流，用于 Compose UI 实时更新
    val allTasks: Flow<List<TaskEntity>> = taskDataStore.data.map { 
        if (it.tasks.isEmpty()) {
            DefaultTasks.initialTasks
        } else {
            it.tasks
        }
    }

    /**
     * 增加任务进度
     */
    suspend fun updateTaskProgress(taskId: String, progress: Double) {
        taskDataStore.updateData { currentPrefs ->
            val tasks = if (currentPrefs.tasks.isEmpty()) DefaultTasks.initialTasks else currentPrefs.tasks
            val updatedTasks = tasks.map { task ->
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

    /**
     * 直接设置任务进度
     */
    suspend fun setTaskProgress(taskId: String, progress: Double) {
        taskDataStore.updateData { currentPrefs ->
            val tasks = if (currentPrefs.tasks.isEmpty()) DefaultTasks.initialTasks else currentPrefs.tasks
            val updatedTasks = tasks.map { task ->
                if (task.id == taskId && task.status == TaskStatus.ACTIVE) {
                    val newProgress = progress.coerceAtMost(task.targetGoal)
                    task.copy(
                        currentProgress = newProgress,
                        status = if (newProgress >= task.targetGoal) TaskStatus.COMPLETED else TaskStatus.ACTIVE
                    )
                } else task
            }
            currentPrefs.copy(tasks = updatedTasks)
        }
    }

    /**
     * 领取奖励
     * @return 领取的任务实体，如果无法领取则返回 null
     */
    suspend fun claimReward(taskId: String): TaskEntity? {
        var claimedTask: TaskEntity? = null
        taskDataStore.updateData { currentPrefs ->
            val tasks = currentPrefs.tasks.ifEmpty { DefaultTasks.initialTasks }
            val updatedTasks = tasks.map {
                if (it.id == taskId && it.status == TaskStatus.COMPLETED) {
                    val updated = it.copy(status = TaskStatus.CLAIMED)
                    claimedTask = updated
                    updated
                } else it
            }
            currentPrefs.copy(tasks = updatedTasks)
        }
        return claimedTask
    }

    /**
     * 检查并解锁新任务（根据游戏进度将 LOCKED 变为 ACTIVE）
     */
    suspend fun checkAndUnlockTasks(totalCash: Double, currentSeries: Int) {
        taskDataStore.updateData { currentPrefs ->
            val tasks = if (currentPrefs.tasks.isEmpty()) DefaultTasks.initialTasks else currentPrefs.tasks
            var changed = false
            val updatedTasks = tasks.map { task ->
                if (task.status == TaskStatus.LOCKED) {
                    val shouldUnlock = when (task.triggerType) {
                        TaskTriggerType.ACCUMULATE_CAPITAL -> totalCash >= task.targetGoal * 0.1 // 提前显示进度？或者达到一定比例解锁
                        else -> task.requiredSeries <= currentSeries
                    }
                    if (shouldUnlock) {
                        changed = true
                        task.copy(status = TaskStatus.ACTIVE)
                    } else task
                } else task
            }
            if (changed) currentPrefs.copy(tasks = updatedTasks) else currentPrefs
        }
    }
}
