package com.example.finances.features.history.ui.models

/**
 * data-класс для хранения информации о расходах/доходах, адаптированной для UI-слоя
 */
data class HistoryRecordUiModel(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: String,
    val amount: String,
    val comment: String?
)
