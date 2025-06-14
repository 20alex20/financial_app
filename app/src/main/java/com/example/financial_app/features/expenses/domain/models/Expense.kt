package com.example.financial_app.features.expenses.domain.models

data class Expense(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String?
)
