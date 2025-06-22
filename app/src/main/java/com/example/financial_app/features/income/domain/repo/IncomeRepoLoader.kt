package com.example.financial_app.features.income.domain.repo

import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.income.domain.models.IncomeRepoData
import com.example.financial_app.features.income.domain.models.RepoIncome

class IncomeRepoLoader {
    fun loadIncomeData(): IncomeRepoData = IncomeRepoData(
        600000.00,
        Currency.RUBLE,
        listOf(
            RepoIncome(0, "Зарплата", 500000.00, null),
            RepoIncome(1, "Подработка", 100000.00, null)
        )
    )
}
