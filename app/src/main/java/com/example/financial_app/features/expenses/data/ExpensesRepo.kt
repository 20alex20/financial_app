package com.example.financial_app.features.expenses.data

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.financial_app.R
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.common.usecase.ShowToastUseCase
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import com.example.financial_app.features.network.data.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesRepo(private val context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepository = AccountRepository(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val showError = ShowToastUseCase(context)
    private val mutex = Mutex()

    private var cachedExpenses: List<Expense> = emptyList()
    private var outdatedExpenses = mutableStateOf(true)
    private var outdatedTotalSpent = mutableStateOf(true)

    suspend fun getCurrentCurrency(): Currency = withContext(Dispatchers.IO) {
        try {
            Currency.parseStr(accountRepository.getAccount().currency)
        } catch (e: Exception) {
            Currency.RUBLE
        }
    }

    private suspend fun loadExpensesFromNetwork(): List<Expense> {
        try {
            val account = accountRepository.getAccount()
            val today = LocalDate.now().format(dateFormatter)
            val transactions = api.getTransactions(
                accountId = account.id,
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
                }.also { cachedExpenses = it }
        } catch (e: Exception) {
            showError(context.getString(R.string.error_loading_data))
            throw e
        }
    }

    private suspend fun getOrLoadExpenses(
        outdatedMain: MutableState<Boolean>,
        outdatedSecond: MutableState<Boolean>
    ): List<Expense> = mutex.withLock {
        if (outdatedMain.value)
            loadExpensesFromNetwork().also { outdatedSecond.value = false }
        else
            cachedExpenses.also { outdatedMain.value = true }
    }

    suspend fun getExpenses(): List<Expense> = withContext(Dispatchers.IO) {
        try {
            getOrLoadExpenses(outdatedExpenses, outdatedTotalSpent)
        } catch (e: Exception) {
            cachedExpenses
        }
    }

    suspend fun getTotalSpent(): Double = withContext(Dispatchers.IO) {
        try {
            getOrLoadExpenses(outdatedTotalSpent, outdatedExpenses).sumOf { it.amount }
        } catch (e: Exception) {
            cachedExpenses.sumOf { it.amount }
        }
    }
}
