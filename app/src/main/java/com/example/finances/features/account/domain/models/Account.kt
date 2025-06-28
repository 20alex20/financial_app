package com.example.finances.features.account.domain.models

import com.example.finances.common.models.Currency

/**
 * data-класс для хранения информации о счете
 */
data class Account(
    val id: Int,
    val balance: Double,
    val currency: Currency
)
