package com.example.finances.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ReloadEventBus {
    private val _events = MutableSharedFlow<ReloadEvent>(replay = 0)
    val events = _events.asSharedFlow()

    suspend fun send(event: ReloadEvent) {
        _events.emit(event)
    }
}
