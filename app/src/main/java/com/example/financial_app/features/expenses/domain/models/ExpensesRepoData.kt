package com.example.financial_app.features.expenses.domain.models

data class ExpensesRepoData(
    val balance: String,
    val expenses: List<Expense>,
)
