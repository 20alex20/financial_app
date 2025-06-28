package com.example.finances.features.history.domain.models

import java.time.LocalDateTime

/**
 * data-класс для хранения информации о расходах/доходах
 */
data class HistoryRecord(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: LocalDateTime,
    val amount: Double,
    val comment: String?
)
