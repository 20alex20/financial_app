package com.example.financial_app.features.check.domain.models

import com.example.financial_app.common.models.Currency

data class CheckRepoData(
    val balance: Double,
    val currency: Currency
)
