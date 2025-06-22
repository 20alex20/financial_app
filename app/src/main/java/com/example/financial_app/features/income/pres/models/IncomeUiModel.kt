package com.example.financial_app.features.income.pres.models

data class IncomeUiModel(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: String,
    val comment: String?
)
