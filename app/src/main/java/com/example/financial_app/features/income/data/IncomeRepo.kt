package com.example.financial_app.features.income.data

import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.income.domain.models.RepoIncome
import com.example.financial_app.features.income.domain.repo.IncomeRepoLoader

object IncomeRepo {
    private var loaded: Boolean = false
    private val loader: IncomeRepoLoader = IncomeRepoLoader()

    private var balance: Double = 0.0
    private var currency: Currency = Currency.RUBLE
    private var income: List<RepoIncome> = listOf()

    private fun load() {
        val data = loader.loadIncomeData()
        balance = data.balance
        currency = data.currency
        income = data.income
        loaded = true
    }

    fun getBalance(): String {
        if (!loaded)
            load()
        return getStringAmount(balance, currency)
    }

    fun getIncome(): List<Income> {
        if (!loaded)
            load()
        return income.map {
            Income(
                it.id,
                it.categoryName,
                getStringAmount(it.amount, currency),
                it.comment
            )
        }
    }
}
