package com.yistudio.capital.engine

import com.yistudio.capital.engine.event.GameEvent
import com.yistudio.capital.engine.event.GameEventBus
import com.yistudio.data.entity.TaskStatus
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
        // 获取当前所有活跃任务进行匹配
        val activeTasks = repository.allTasks.first().filter { it.status == TaskStatus.ACTIVE }

        activeTasks.forEach { task ->
            when (event) {
                is GameEvent.BusinessUpgraded -> {
                    // 逻辑：如果任务 ID 与业务 ID 相关联
                    if (task.id.contains(event.businessId)) {
                        repository.updateTaskProgress(task.id, event.newLevel.toLong())
                    }
                }
                is GameEvent.CapitalChanged -> {
                    // 逻辑：资产类任务通常是“到达即完成”，进度直接设为当前值
                    if (task.id.startsWith("cap_")) {
                        repository.updateTaskProgress(task.id, event.totalCapital)
                    }
                }
                is GameEvent.ManualClick -> {
                    if (task.id == "daily_click_task") {
                        repository.updateTaskProgress(task.id, 1L)
                    }
                }
                else -> { /* 处理其他事件 */ }
            }
        }
    }
}