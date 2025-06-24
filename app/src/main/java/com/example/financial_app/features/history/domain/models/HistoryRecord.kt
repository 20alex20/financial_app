package com.example.financial_app.features.history.domain.models

import java.time.LocalDateTime

data class HistoryRecord(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: LocalDateTime,
    val amount: Double,
    val comment: String?
)
