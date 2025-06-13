package com.example.financial_app.features.expenses.domain.models

data class Expense(
    val categoryId: Int,
    val categoryName: String,
    val amount: String,
    val emoji: String?,
    val comment: String?
)
