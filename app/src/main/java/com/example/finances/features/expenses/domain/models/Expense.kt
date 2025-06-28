package com.example.finances.features.expenses.domain.models

/**
 * data-класс для хранения информации о расходах
 */
data class Expense(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val comment: String?
)
