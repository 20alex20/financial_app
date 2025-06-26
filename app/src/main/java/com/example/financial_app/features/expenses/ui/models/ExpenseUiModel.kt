package com.example.financial_app.features.expenses.ui.models

data class ExpenseUiModel(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String?
) 