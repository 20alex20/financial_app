package com.example.financial_app.features.expenses.data

import android.content.Context
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import com.example.financial_app.features.network.data.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesRepo(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepository = AccountRepository(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var cachedExpenses: List<Expense>? = null

    suspend fun getCurrency(): Response<Currency> = withContext(Dispatchers.IO) {
        try {
            Response.Success(Currency.parseStr(accountRepository.getAccount().currency))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getExpenses(): Response<List<Expense>> = withContext(Dispatchers.IO) {
        try {
            if (cachedExpenses != null)
                return@withContext Response.Success(cachedExpenses ?: emptyList())

            val account = accountRepository.getAccount()
            val today = LocalDate.now().format(dateFormatter)
            val transactions = api.getTransactions(
                accountId = account.id,
                startDate = today,
                endDate = today
            )

            Response.Success(
                transactions
                    .filterNot { it.category.isIncome }
                    .sortedByDescending { it.transactionDate }
                    .map { transaction ->
                        Expense(
                            id = transaction.id,
                            categoryName = transaction.category.name,
                            categoryEmoji = transaction.category.emoji,
                            amount = transaction.amount.toDouble(),
                            comment = transaction.comment
                        )
                    }
                    .also { cachedExpenses = it }
            )
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    fun clearCache() {
        cachedExpenses = null
    }
}
