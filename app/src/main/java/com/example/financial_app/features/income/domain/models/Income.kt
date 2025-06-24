package com.example.financial_app.features.income.domain.models

data class Income(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val comment: String?
)
