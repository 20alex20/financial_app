package com.example.financial_app.features.expenses.data

import com.example.financial_app.common.code.getStringAmount
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.features.expenses.domain.models.RepoExpense
import com.example.financial_app.features.expenses.domain.repo.ExpensesRepoLoader

object ExpensesRepo {
    private var loaded: Boolean = false
    private val loader: ExpensesRepoLoader = ExpensesRepoLoader()

    private var balance: Double = 0.0
    private var currency: Currency = Currency.RUBLE
    private var expenses: List<RepoExpense> = listOf()

    private fun load() {
        val data = loader.loadExpensesData()
        balance = data.balance
        currency = data.currency
        expenses = data.expenses
        loaded = true
    }

    fun getBalance(): String {
        if (!loaded)
            load()
        return getStringAmount(balance, currency)
    }

    fun getExpenses(): List<Expense> {
        if (!loaded)
            load()
        return expenses.map {
            Expense(
                it.id,
                it.categoryName,
                it.categoryEmoji,
                getStringAmount(it.amount, currency),
                it.comment
            )
        }
    }
}
