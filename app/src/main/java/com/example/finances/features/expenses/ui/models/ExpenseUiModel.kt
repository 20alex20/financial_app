package com.example.finances.features.expenses.ui.models

/**
 * data-класс для хранения информации о расходах, адаптированной для UI-слоя
 */
data class ExpenseUiModel(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String?
)
