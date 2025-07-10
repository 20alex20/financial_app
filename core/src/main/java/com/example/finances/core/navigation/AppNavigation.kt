package com.example.finances.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(
    navController: NavHostController,
    accountNavigation: (NavHostController) -> Unit,
    transactionsNavigation: (NavHostController) -> Unit,
    categoriesNavigation: (NavHostController) -> Unit,
    settingsNavigation: (NavHostController) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavBarRoutes.Account::class.java.simpleName
    ) {
        composable(NavBarRoutes.Account::class.java.simpleName) {
            accountNavigation(navController)
        }
        composable(NavBarRoutes.Transactions::class.java.simpleName) {
            transactionsNavigation(navController)
        }
        composable(NavBarRoutes.Categories::class.java.simpleName) {
            categoriesNavigation(navController)
        }
        composable(NavBarRoutes.Settings::class.java.simpleName) {
            settingsNavigation(navController)
        }
    }
} 