package com.example.finances.feature.account.navigation

import kotlinx.serialization.Serializable

/**
 * Запечатанный класс содержит объекты, соответствующие экранам и используемые для навигации
 */
sealed interface AccountNavRoutes {
    @Serializable data object Account : AccountNavRoutes
    @Serializable data object EditAccount : AccountNavRoutes
}
