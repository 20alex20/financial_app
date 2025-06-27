package com.example.financial_app.features.navigation.data

sealed class NavRoutes(val route: String) {
    data object Expenses : NavRoutes("expenses")
    data object Income : NavRoutes("income")
    data object Account : NavRoutes("account")
    data object Categories : NavRoutes("categories")
    data object Settings : NavRoutes("settings")
    data object History : NavRoutes("history")
}
