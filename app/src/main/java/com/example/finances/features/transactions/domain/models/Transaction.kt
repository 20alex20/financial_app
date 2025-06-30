package com.example.finances.features.transactions.domain.models

import java.time.LocalDateTime

data class Transaction(
    val id: Int,
    val categoryName: String,
    val categoryEmoji: String,
    val dateTime: LocalDateTime,
    val amount: Double,
    val comment: String?
)
