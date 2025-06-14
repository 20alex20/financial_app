package com.example.financial_app.features.income.domain.models

data class Income(
    val id: Int,
    val categoryName: String,
    val amount: String,
    val comment: String?
)
