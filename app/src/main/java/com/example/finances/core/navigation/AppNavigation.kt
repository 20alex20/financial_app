package com.example.finances.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finances.core.ui.components.BottomNavigationBar
import com.example.finances.features.account.navigation.AccountNavigation
import com.example.finances.features.categories.ui.CategoriesScreen
import com.example.finances.features.settings.ui.SettingsScreen
import com.example.finances.features.transactions.navigation.TransactionsNavigation

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Column(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = NavBarRoutes.Expenses,
            modifier = Modifier.weight(1f)
        ) {
            composable<NavBarRoutes.Expenses> {
                TransactionsNavigation(isIncome = false)
            }
            composable<NavBarRoutes.Income> {
                TransactionsNavigation(isIncome = true)
            }
            composable<NavBarRoutes.Account> {
                AccountNavigation()
            }
            composable<NavBarRoutes.Categories> {
                CategoriesScreen()
            }
            composable<NavBarRoutes.Settings> {
                SettingsScreen()
            }
        }

        BottomNavigationBar(
            navController = navController,
            navBarItems = navBarItems(),
            modifier = Modifier.height(80.dp)
        )
    }
}
