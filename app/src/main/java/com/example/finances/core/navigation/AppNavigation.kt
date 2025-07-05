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
import com.example.finances.features.account.ui.EditAccountScreen
import com.example.finances.features.account.ui.AccountScreen
import com.example.finances.features.categories.ui.CategoriesScreen
import com.example.finances.features.transactions.ui.HistoryScreen
import com.example.finances.features.settings.ui.SettingsScreen
import com.example.finances.features.transactions.ui.ExpensesIncomeScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Column(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Expenses.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(NavRoutes.Expenses.route) {
                ExpensesIncomeScreen(NavRoutes.Expenses.route, navController)
            }
            composable(NavRoutes.Expenses.route + "/" + NavRoutes.History.route) {
                HistoryScreen(NavRoutes.Expenses.route, navController)
            }
            composable(NavRoutes.Income.route) {
                ExpensesIncomeScreen(NavRoutes.Income.route, navController)
            }
            composable(NavRoutes.Income.route + "/" + NavRoutes.History.route) {
                HistoryScreen(NavRoutes.Income.route, navController)
            }
            composable(NavRoutes.Account.route) {
                AccountScreen(navController)
            }
            composable(NavRoutes.EditAccount.route) {
                EditAccountScreen(navController)
            }
            composable(NavRoutes.Categories.route) {
                CategoriesScreen()
            }
            composable(NavRoutes.Settings.route) { SettingsScreen() }
        }

        BottomNavigationBar(
            navController = navController,
            navBarItems = navBarItems(),
            modifier = Modifier.height(80.dp)
        )
    }
}
