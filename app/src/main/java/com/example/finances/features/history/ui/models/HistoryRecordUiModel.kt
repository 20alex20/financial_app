package com.example.finances.features.history.ui.models

data class HistoryRecordUiModel(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: String,
    val amount: String,
    val comment: String?
)
