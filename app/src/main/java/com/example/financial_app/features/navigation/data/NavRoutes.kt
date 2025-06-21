package com.example.financial_app.features.navigation.data

sealed class NavRoutes(val route: String) {
    object Expenses : NavRoutes("expenses")
    object Income : NavRoutes("income")
    object Check : NavRoutes("check")
    object Categories : NavRoutes("categories")
    object Settings : NavRoutes("settings")
    object History : NavRoutes("expenses/history")
}
