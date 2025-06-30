package com.example.finances.features.transactions.ui.models

/**
 * Стейт для данных вьюмодели экрана истории
 */
data class HistoryViewModelState(
    val total: String,
    val history: List<HistoryRecord>
)
