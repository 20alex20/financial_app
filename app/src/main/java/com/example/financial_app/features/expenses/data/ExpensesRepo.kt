package com.example.financial_app.features.expenses.data

import android.content.Context
import com.example.financial_app.R
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.features.expenses.domain.usecase.ShowErrorUseCase
import com.example.financial_app.features.network.domain.NetworkModule
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesRepo(private val context: Context) {
    private val api = NetworkModule.provideApi(context, FinanceApi::class.java)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val showError = ShowErrorUseCase(context)

    private var cachedExpenses: List<Expense>? = null
    private var lastLoadDate: LocalDate? = null
    private var outdatedExpenses = false
    private var outdatedTotalSpent = false
    private var currentCurrency: Currency? = null

    private fun parseCurrency(currencyStr: String): Currency = when (currencyStr) {
        "RUB" -> Currency.RUBLE
        "USD" -> Currency.DOLLAR
        "EUR" -> Currency.EURO
        else -> Currency.RUBLE
    }

    private suspend fun loadExpensesFromNetwork(): List<Expense> {
        try {
            val accounts = api.getAccounts()
            val firstAccount = accounts.firstOrNull() ?: return emptyList()

            currentCurrency = parseCurrency(firstAccount.currency)
            
            val today = LocalDate.now().format(dateFormatter)
            val transactions = api.getTransactions(
                accountId = firstAccount.id,
                startDate = today,
                endDate = today
            )

            return transactions
                .filterNot { it.category.isIncome }
                .map { transaction ->
                    Expense(
                        id = transaction.id,
                        categoryName = transaction.category.name,
                        categoryEmoji = transaction.category.emoji,
                        amount = transaction.amount.toDouble(),
                        comment = transaction.comment
                    )
                }.also { expenses ->
                    cachedExpenses = expenses
                    lastLoadDate = LocalDate.now()
                }
        } catch (e: Exception) {
            showError(context.getString(R.string.error_loading_data))
            throw e
        }
    }

    private fun isCacheValid(): Boolean {
        val today = LocalDate.now()
        return cachedExpenses != null && lastLoadDate == today
    }

    suspend fun getExpenses(): List<Expense> = withContext(Dispatchers.IO) {
        try {
            if (!isCacheValid() || !outdatedExpenses) {
                loadExpensesFromNetwork().also { outdatedTotalSpent = true }
            } else {
                outdatedExpenses = false
                cachedExpenses ?: emptyList()
            }
        } catch (e: Exception) {
            cachedExpenses ?: emptyList()
        }
    }

    suspend fun getTotalSpent(): Double = withContext(Dispatchers.IO) {
        try {
            val expenses = if (!isCacheValid() || !outdatedTotalSpent) {
                loadExpensesFromNetwork().also { outdatedExpenses = true }
            } else {
                outdatedTotalSpent = false
                cachedExpenses ?: emptyList()
            }
            expenses.sumOf { it.amount }
        } catch (e: Exception) {
            cachedExpenses?.sumOf { it.amount } ?: 0.0
        }
    }

    fun getCurrentCurrency(): Currency = currentCurrency ?: Currency.RUBLE
}
