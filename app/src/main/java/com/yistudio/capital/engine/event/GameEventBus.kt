package com.yistudio.capital.engine.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<GameEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    suspend fun post(event: GameEvent) {
        _events.emit(event)
    }
}