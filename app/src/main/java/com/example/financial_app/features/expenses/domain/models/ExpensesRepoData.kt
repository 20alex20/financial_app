package com.example.financial_app.features.expenses.domain.models

import com.example.financial_app.common.models.Currency

data class ExpensesRepoData(
    val balance: Double,
    val currency: Currency,
    val expenses: List<RepoExpense>,
)

data class RepoExpense(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val comment: String?
)
