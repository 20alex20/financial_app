package com.example.finances.feature.account.domain.models

import com.example.finances.core.utils.models.Currency

data class Account(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: Currency
)
