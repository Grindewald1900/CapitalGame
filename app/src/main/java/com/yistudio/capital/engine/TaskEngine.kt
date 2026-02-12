package com.yistudio.capital.engine

import com.yistudio.capital.engine.event.GameEvent
import com.yistudio.capital.engine.event.GameEventBus
import com.yistudio.data.entity.TaskStatus
import com.yistudio.data.entity.TaskTriggerType
import com.yistudio.data.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskEngine @Inject constructor(
    private val eventBus: GameEventBus,
    private val repository: TaskRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    init {
        startSensing()
    }

    private fun startSensing() {
        scope.launch {
            eventBus.events.collect { event ->
                handleEvent(event)
            }
        }
    }

    private suspend fun handleEvent(event: GameEvent) {
        // 1. 更新活跃任务进度
        val activeTasks = repository.allTasks.first().filter { it.status == TaskStatus.ACTIVE }

        activeTasks.forEach { task ->
            when (event) {
                is GameEvent.BusinessUpgraded -> {
                    if (task.triggerType == TaskTriggerType.BUSINESS_LEVEL) {
                        if (task.targetId == null || task.targetId == event.businessId) {
                            repository.setTaskProgress(task.id, event.newLevel.toDouble())
                        }
                    }
                }
                is GameEvent.CapitalChanged -> {
                    if (task.triggerType == TaskTriggerType.ACCUMULATE_CAPITAL) {
                        repository.setTaskProgress(task.id, event.totalCapital)
                    }
                }
                is GameEvent.ManualClick -> {
                    if (task.triggerType == TaskTriggerType.TOTAL_CLICKS) {
                        repository.updateTaskProgress(task.id, 1.0)
                    }
                }
                else -> {}
            }
        }

        // 2. 检查是否有新任务可以从 LOCKED 变为 ACTIVE
        // 这里需要一些全局状态，或者我们可以从 repository 获取
        // 为了性能，我们不需要每次事件都检查，但目前先这样做
        if (event is GameEvent.CapitalChanged || event is GameEvent.BusinessUpgraded) {
            // 我们需要 totalCash 和 currentSeries
            // 这里简单处理，具体逻辑可以在 repository.checkAndUnlockTasks 中根据最新数据判断
            // 目前 checkAndUnlockTasks 的参数是外部传进去的，我们可能需要调整它或者在 ViewModel 中调用
        }
    }
}
