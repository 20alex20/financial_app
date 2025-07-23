package com.example.finances.feature.transactions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.finances.feature.transactions.ui.AnalysisScreen
import com.example.finances.feature.transactions.ui.CreateUpdateScreen
import com.example.finances.feature.transactions.ui.ExpensesIncomeScreen
import com.example.finances.feature.transactions.ui.HistoryScreen

fun transactionsNavigation(
    navGraphBuilder: NavGraphBuilder,
    navController: NavHostController
) = navGraphBuilder.apply {
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
