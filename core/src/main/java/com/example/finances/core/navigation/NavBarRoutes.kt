package com.example.finances.core.navigation

import kotlinx.serialization.Serializable

sealed interface NavBarRoutes {
    @Serializable data object Expenses : NavBarRoutes
    @Serializable data object Income : NavBarRoutes
    @Serializable data object Account : NavBarRoutes
    @Serializable data object Categories : NavBarRoutes
    @Serializable data object Settings : NavBarRoutes
}
