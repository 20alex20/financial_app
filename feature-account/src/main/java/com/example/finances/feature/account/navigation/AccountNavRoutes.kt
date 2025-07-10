package com.example.finances.feature.account.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AccountNavRoutes(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Account : AccountNavRoutes("account")

    object EditAccount : AccountNavRoutes(
        route = "account/edit/{accountId}",
        arguments = listOf(
            navArgument("accountId") {
                type = NavType.StringType
                nullable = false
            }
        )
    ) {
        fun createRoute(accountId: String) = "account/edit/$accountId"
    }
} 