package com.example.financial_app.features.income.data

import android.content.Context
import com.example.financial_app.R
import com.example.financial_app.common.usecase.ShowToastUseCase
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.network.data.AccountRepository
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeRepo(private val context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepository = AccountRepository(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val showError = ShowToastUseCase(context)

    private var cachedIncome: List<Income>? = null

    suspend fun getCurrency(): Currency = withContext(Dispatchers.IO) {
        try {
            Currency.parseStr(accountRepository.getAccount().currency)
        } catch (e: Exception) {
            Currency.RUBLE
        }
    }

    suspend fun getIncome(): List<Income> = withContext(Dispatchers.IO) {
        try {
            if (cachedIncome != null)
                return@withContext cachedIncome ?: emptyList()

            val account = accountRepository.getAccount()
            val today = LocalDate.now().format(dateFormatter)
            val transactions = api.getTransactions(
                accountId = account.id,
                startDate = today,
                endDate = today
            )

            transactions
                .filter { it.category.isIncome }
                .sortedByDescending { it.transactionDate }
                .map { transaction ->
                    Income(
                        id = transaction.id,
                        categoryName = transaction.category.name,
                        categoryEmoji = transaction.category.emoji,
                        amount = transaction.amount.toDouble(),
                        comment = transaction.comment
                    )
                }
                .also { cachedIncome = it }
        } catch (e: Exception) {
            showError(context.getString(R.string.error_loading_data))
            emptyList()
        }
    }

    fun clearCache() {
        cachedIncome = null
    }
}
