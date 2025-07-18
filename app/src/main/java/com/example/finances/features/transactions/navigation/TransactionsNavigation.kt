package com.example.finances.features.transactions.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.finances.features.transactions.ui.AnalysisScreen
import com.example.finances.features.transactions.ui.CreateUpdateScreen
import com.example.finances.features.transactions.ui.ExpensesIncomeScreen
import com.example.finances.features.transactions.ui.HistoryScreen

@Composable
fun TransactionsNavigation(isIncome: Boolean, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = TransactionsNavRoutes.ExpensesIncome(isIncome),
        modifier = modifier.fillMaxSize()
    ) {
        composable<TransactionsNavRoutes.ExpensesIncome> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.CreateUpdate>()
            ExpensesIncomeScreen(ScreenType.fromBoolean(args.isIncome), navController)
        }
        composable<TransactionsNavRoutes.History> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.CreateUpdate>()
            HistoryScreen(ScreenType.fromBoolean(args.isIncome), navController)
        }
        composable<TransactionsNavRoutes.Analysis> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.Analysis>()
            AnalysisScreen(ScreenType.fromBoolean(args.isIncome), navController)
        }
        composable<TransactionsNavRoutes.CreateUpdate> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.CreateUpdate>()
            CreateUpdateScreen(
                screenType = ScreenType.fromBoolean(args.isIncome),
                transactionId = args.transactionId,
                navController = navController
            )
        }
    }
}
