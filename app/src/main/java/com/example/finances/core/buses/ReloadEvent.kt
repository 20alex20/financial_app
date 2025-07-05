package com.example.finances.core.buses

/**
 * Запечатанный класс содержит объекты, соответствующие типам событий,
 * требующих перезагрузки содержимого экранов
 */
sealed interface ReloadEvent {
    data object AccountUpdated : ReloadEvent
}
