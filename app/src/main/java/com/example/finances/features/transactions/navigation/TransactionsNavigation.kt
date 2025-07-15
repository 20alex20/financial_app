package com.example.finances.features.transactions.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.finances.features.transactions.domain.ScreenType
import com.example.finances.features.transactions.ui.CreateUpdateScreen
import com.example.finances.features.transactions.ui.ExpensesIncomeScreen
import com.example.finances.features.transactions.ui.HistoryScreen

@Composable
fun TransactionsNavigation(isIncome: Boolean, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val screenType = if (isIncome) ScreenType.Income else ScreenType.Expenses
    NavHost(
        navController = navController,
        startDestination = TransactionsNavRoutes.ExpensesIncome(screenType),
        modifier = modifier.fillMaxSize()
    ) {
        composable<TransactionsNavRoutes.ExpensesIncome> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.CreateUpdate>()
            ExpensesIncomeScreen(args.screenType, navController)
        }
        composable<TransactionsNavRoutes.History> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.CreateUpdate>()
            HistoryScreen(args.screenType, navController)
        }
        composable<TransactionsNavRoutes.CreateUpdate> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.CreateUpdate>()
            CreateUpdateScreen(args.screenType, args.transactionId, navController)
        }
    }
}
