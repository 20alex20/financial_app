package com.example.financial_app.features.account.domain.models

import com.example.financial_app.common.models.Currency

data class Account(
    val id: Int,
    val balance: Double,
    val currency: Currency
)
