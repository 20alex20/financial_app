package com.example.finances.feature.transactions.ui.models

data class HistoryViewModelState(
    val total: String,
    val history: List<HistoryRecord>
)
