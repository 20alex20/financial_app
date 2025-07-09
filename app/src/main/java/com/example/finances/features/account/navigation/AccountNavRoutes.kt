package com.example.finances.features.account.navigation

/**
 * Запечатанный класс содержит объекты, соответствующие экранам и используемые для навигации
 */
sealed interface AccountNavRoutes {
    data object Account : AccountNavRoutes
    data object EditAccount : AccountNavRoutes
}
