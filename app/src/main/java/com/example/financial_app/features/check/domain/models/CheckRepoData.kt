package com.example.financial_app.features.check.domain.models

import com.example.financial_app.features.network.data.models.Currency

data class CheckRepoData(
    val balance: Double,
    val currency: Currency
)
