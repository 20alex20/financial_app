package com.example.finances.core.navigation

/**
 * Запечатанный класс содержит объекты, соответствующие экранам и используемые для навигации
 */
sealed interface NavBarRoutes {
    data object Expenses : NavBarRoutes
    data object Income : NavBarRoutes
    data object Account : NavBarRoutes
    data object Categories : NavBarRoutes
    data object Settings :NavBarRoutes
}
