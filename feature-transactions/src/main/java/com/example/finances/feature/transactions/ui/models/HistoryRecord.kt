package com.example.finances.feature.transactions.ui.models

data class HistoryRecord(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: String,
    val amount: String,
    val comment: String
)
