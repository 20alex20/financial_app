package com.example.finances.core.buses

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Особый объект шина-поток для получения сообщений о событиях,
 * требующих перезагрузки содержимого экранов
 */
object ReloadEventBus {
    private val _events = MutableSharedFlow<ReloadEvent>(replay = 0)
    val events = _events.asSharedFlow()

    suspend fun send(event: ReloadEvent) {
        _events.emit(event)
    }
}
