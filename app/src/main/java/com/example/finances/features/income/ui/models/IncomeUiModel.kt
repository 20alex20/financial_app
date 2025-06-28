package com.example.finances.features.income.ui.models

/**
 * data-класс для хранения информации о доходах, адаптированной для UI-слоя
 */
data class IncomeUiModel(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String?
)
