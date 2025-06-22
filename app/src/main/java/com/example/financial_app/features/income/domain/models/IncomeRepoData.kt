package com.example.financial_app.features.income.domain.models

import com.example.financial_app.features.network.data.models.Currency

data class IncomeRepoData(
    val balance: Double,
    val currency: Currency,
    val income: List<RepoIncome>,
)

data class RepoIncome(
    val id: Int,
    val categoryName: String,
    val amount: Double,
    val comment: String?
)
