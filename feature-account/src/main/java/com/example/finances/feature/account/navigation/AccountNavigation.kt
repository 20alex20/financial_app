package com.example.finances.feature.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.finances.feature.account.ui.AccountScreen
import com.example.finances.feature.account.ui.EditAccountScreen

fun accountNavigation(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController
) = navGraphBuilder.apply {
    composable<AccountNavRoutes.Account> {
        AccountScreen(navController)
    }
    composable<AccountNavRoutes.EditAccount> {
        EditAccountScreen(navController)
    }
}
