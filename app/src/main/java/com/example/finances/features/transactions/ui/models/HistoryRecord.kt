package com.example.finances.features.transactions.ui.models

/**
 * data-класс для хранения информации о расходах/доходах, адаптированной для UI-слоя
 */
data class HistoryRecord(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: String,
    val amount: String,
    val comment: String?
)
