package com.example.finances.features.transactions.ui.models

data class ExpenseIncome(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String
)
