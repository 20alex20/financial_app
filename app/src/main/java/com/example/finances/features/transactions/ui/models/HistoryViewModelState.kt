package com.example.finances.features.transactions.ui.models

data class HistoryViewModelState(
    val total: String,
    val history: List<HistoryRecord>
)
