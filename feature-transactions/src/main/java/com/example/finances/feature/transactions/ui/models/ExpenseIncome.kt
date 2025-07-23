package com.example.finances.feature.transactions.ui.models

data class ExpenseIncome(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String
)
