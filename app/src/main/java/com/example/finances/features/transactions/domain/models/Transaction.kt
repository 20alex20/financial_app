package com.example.finances.features.transactions.domain.models

/**
 * data-класс для хранения информации о расходах
 */
data class Transaction(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val amount: Double,
    val comment: String?
)
