package com.example.finances.features.income.domain.models

/**
 * data-класс для хранения информации о доходах
 */
data class Income(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val comment: String?
)
