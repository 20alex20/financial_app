package com.example.finances.feature.transactions.domain.models

data class AnalysisGroup(
    val name: String,
    val emoji: String,
    val percent: Double,
    val amount: Double
)
