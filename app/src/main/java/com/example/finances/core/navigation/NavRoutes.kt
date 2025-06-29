package com.example.finances.core.navigation

/**
 * Запечатанный класс содержит объекты, соответствующие экранам и используемые для навигации
 */
sealed class NavRoutes(val route: String) {
    data object Expenses : NavRoutes("expenses")
    data object Income : NavRoutes("income")
    data object Account : NavRoutes("account")
    data object Categories : NavRoutes("categories")
    data object Settings : NavRoutes("settings")
    data object History : NavRoutes("history")
}
