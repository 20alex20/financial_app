package com.example.financial_app.features.history.domain.models

import com.example.financial_app.common.models.Currency

data class HistoryRepoData(
    val startDateTime: String,
    val endDateTime: String,
    val sum: Double,
    val currency: Currency,
    val history: List<RepoHistoryRecord>,
)

data class RepoHistoryRecord(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: String,
    val amount: Double,
    val comment: String?
)
