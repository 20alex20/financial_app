package com.example.finances.features.account.domain.models

import com.example.finances.core.domain.models.Currency

data class Account(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: Currency
)
