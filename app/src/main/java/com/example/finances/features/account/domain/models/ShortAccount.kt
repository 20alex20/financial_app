package com.example.finances.features.account.domain.models

import com.example.finances.core.data.repository.models.Currency

data class ShortAccount(
    val name: String,
    val balance: Double,
    val currency: Currency
)
