package com.example.finances.core.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NavBarRoutes : Parcelable {
    @Parcelize
    data object Account : NavBarRoutes()
    @Parcelize
    data object Transactions : NavBarRoutes()
    @Parcelize
    data object Categories : NavBarRoutes()
    @Parcelize
    data object Settings : NavBarRoutes()
    @Parcelize
    data object History : NavBarRoutes()
} 