package com.example.finances.features.transactions.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.ui.CreateUpdateScreen
import com.example.finances.features.transactions.ui.ExpensesIncomeScreen
import com.example.finances.features.transactions.ui.HistoryScreen

@Composable
fun TransactionsNavigation(isIncome: Boolean, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val mainScreen = if (isIncome) TransactionsNavRoutes.Income else TransactionsNavRoutes.Expenses
    NavHost(
        navController = navController,
        startDestination = mainScreen,
        modifier = modifier.fillMaxSize()
    ) {
        if (isIncome) composable<TransactionsNavRoutes.Income> {
            ExpensesIncomeScreen(true, navController)
        } else composable<TransactionsNavRoutes.Expenses> {
            ExpensesIncomeScreen(false, navController)
        }

        if (isIncome) composable<TransactionsNavRoutes.IncomeHistory> {
            HistoryScreen(true, navController)
        } else composable<TransactionsNavRoutes.ExpensesHistory> {
            HistoryScreen(false, navController)
        }

        if (isIncome) composable<TransactionsNavRoutes.IncomeCreateUpdate> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.IncomeCreateUpdate>()
            TransactionsRepo.incomeTransactionId = args.transactionId
            CreateUpdateScreen(true, navController)
        } else composable<TransactionsNavRoutes.ExpensesCreateUpdate> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionsNavRoutes.ExpensesCreateUpdate>()
            TransactionsRepo.expenseTransactionId = args.transactionId
            CreateUpdateScreen(false, navController)
        }
    }
}
