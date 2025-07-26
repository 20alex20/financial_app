package com.example.finances.feature.account.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finances.feature.account.ui.AccountScreen
import com.example.finances.feature.account.ui.EditAccountScreen

@Composable
fun AccountNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AccountNavRoutes.Account,
        modifier = modifier.fillMaxSize()
    ) {
        composable<AccountNavRoutes.Account> {
            AccountScreen(navController)
        }
        composable<AccountNavRoutes.EditAccount> {
            EditAccountScreen(navController)
        }
    }
}
