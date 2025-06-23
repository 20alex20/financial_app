package com.example.financial_app.features.income.data

import android.content.Context
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.network.data.AccountRepository
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeRepo(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepository = AccountRepository(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var cachedIncome: List<Income>? = null

    suspend fun getCurrency(): Response<Currency> = withContext(Dispatchers.IO) {
        try {
            Response.Success(Currency.parseStr(accountRepository.getAccount().currency))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getIncome(): Response<List<Income>> = withContext(Dispatchers.IO) {
        try {
            if (cachedIncome != null)
                return@withContext Response.Success(cachedIncome ?: emptyList())

            val account = accountRepository.getAccount()
            val today = LocalDate.now().format(dateFormatter)
            val transactions = api.getTransactions(
                accountId = account.id,
                startDate = today,
                endDate = today
            )

            Response.Success(
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
            )
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    fun clearCache() {
        cachedIncome = null
    }
}
