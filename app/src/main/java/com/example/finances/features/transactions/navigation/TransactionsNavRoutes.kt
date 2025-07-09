package com.example.finances.features.transactions.navigation

/**
 * Запечатанный класс содержит объекты, соответствующие экранам и используемые для навигации
 */
sealed interface TransactionsNavRoutes {
    data object Expenses : TransactionsNavRoutes
    data object ExpensesHistory : TransactionsNavRoutes
    data object Income : TransactionsNavRoutes
    data object IncomeHistory : TransactionsNavRoutes
}
