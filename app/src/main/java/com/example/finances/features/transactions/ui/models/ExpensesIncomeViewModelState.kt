package com.example.finances.features.transactions.ui.models

/**
 * Стейт для данных вьюмодели экрана расходов/доходов
 */
data class ExpensesIncomeViewModelState(
    val total: String,
    val expensesIncome: List<ExpenseIncome>
)
