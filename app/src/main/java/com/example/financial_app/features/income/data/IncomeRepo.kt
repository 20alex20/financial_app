package com.example.financial_app.features.income.data

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.financial_app.R
import com.example.financial_app.common.usecase.ShowToastUseCase
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.network.data.AccountRepository
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeRepo(private val context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepository = AccountRepository(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val showError = ShowToastUseCase(context)
    private val mutex = Mutex()

    private var cachedIncome: List<Income> = emptyList()
    private var outdatedIncome = mutableStateOf(true)
    private var outdatedTotalSpent = mutableStateOf(true)

    suspend fun getCurrentCurrency(): Currency = withContext(Dispatchers.IO) {
        try {
            Currency.parseStr(accountRepository.getAccount().currency)
        } catch (e: Exception) {
            Currency.RUBLE
        }
    }

    private suspend fun loadIncomeFromNetwork(): List<Income> {
        try {
            val account = accountRepository.getAccount()
            val today = LocalDate.now().format(dateFormatter)
            val transactions = api.getTransactions(
                accountId = account.id,
                startDate = today,
                endDate = today
            )

            return transactions
                .filter { it.category.isIncome }
                .map { transaction ->
                    Income(
                        id = transaction.id,
                        categoryName = transaction.category.name,
                        categoryEmoji = transaction.category.emoji,
                        amount = transaction.amount.toDouble(),
                        comment = transaction.comment
                    )
                }.also { cachedIncome = it }
        } catch (e: Exception) {
            showError(context.getString(R.string.error_loading_data))
            throw e
        }
    }

    private suspend fun getOrLoadIncome(
        outdatedMain: MutableState<Boolean>,
        outdatedSecond: MutableState<Boolean>
    ): List<Income> = mutex.withLock {
        if (outdatedMain.value)
            loadIncomeFromNetwork().also { outdatedSecond.value = false }
        else
            cachedIncome.also { outdatedMain.value = true }
    }

    suspend fun getIncome(): List<Income> = withContext(Dispatchers.IO) {
        try {
            getOrLoadIncome(outdatedIncome, outdatedTotalSpent)
        } catch (e: Exception) {
            cachedIncome
        }
    }

    suspend fun getTotalSpent(): Double = withContext(Dispatchers.IO) {
        try {
            getOrLoadIncome(outdatedTotalSpent, outdatedIncome).sumOf { it.amount }
        } catch (e: Exception) {
            cachedIncome.sumOf { it.amount }
        }
    }
}
