package com.example.financial_app.features.check.data

import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.check.domain.repo.CheckRepoLoader

object CheckRepo {
    private var loaded: Boolean = false
    private val loader: CheckRepoLoader = CheckRepoLoader()

    private var balance: Double = 0.0
    private var currency: Currency = Currency.RUBLE

    private fun load() {
        val data = loader.loadCheckData()
        balance = data.balance
        currency = data.currency
        loaded = true
    }

    fun getBalance(): String {
        if (!loaded)
            load()
        return currency.getStrAmount(balance)
    }

    fun getCurrency(): String {
        if (!loaded)
            load()
        return currency.symbol
    }
}
