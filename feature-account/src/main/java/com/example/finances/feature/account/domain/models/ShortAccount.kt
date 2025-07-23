package com.example.finances.feature.account.domain.models

import com.example.finances.core.utils.models.Currency

data class ShortAccount(
    val name: String,
    val balance: Double,
    val currency: Currency
)
