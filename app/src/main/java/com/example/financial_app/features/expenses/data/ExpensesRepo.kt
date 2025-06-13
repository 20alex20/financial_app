package com.example.financial_app.features.expenses.data

import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.features.expenses.domain.repo.ExpensesRepoLoader

object ExpensesRepo {
    private var loaded: Boolean = false
    private val loader: ExpensesRepoLoader = ExpensesRepoLoader()

    private var balance: String = "0 ₽"
    private var expenses: List<Expense> = listOf()

    fun getBalance(): String {
        if (!loaded) {
            val data = loader.loadExpensesData()
            balance = data.balance
            expenses = data.expenses
            loaded = true
        }
        return balance
    }

    fun getExpenses(): List<Expense> {
        if (!loaded) {
            val data = loader.loadExpensesData()
            balance = data.balance
            expenses = data.expenses
            loaded = true
        }
        return expenses
    }
}
