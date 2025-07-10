package com.example.finances.feature.transactions.navigation

sealed class TransactionsNavRoutes(val route: String) {
    object ExpensesIncome : TransactionsNavRoutes("transactions/expenses-income")
    object History : TransactionsNavRoutes("transactions/history")
} 