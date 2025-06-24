package com.example.financial_app.features.expenses.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.expenses.domain.models.Expense
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import com.example.financial_app.features.network.data.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesRepo(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepo = AccountRepository.init(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var cachedExpenses: List<Expense>? = null

    suspend fun getCurrency(): Response<Currency> = withContext(Dispatchers.IO) {
        val strCurrency = accountRepo.getAccount()?.currency
        if (strCurrency != null)
            Response.Success(Currency.parseStr(strCurrency))
        else
            Response.Failure(Exception("Error loading account data"))
    }

    fun getExpenses(): Flow<Response<List<Expense>>> = repoTryCatchBlock {
        if (cachedExpenses != null)
            return@repoTryCatchBlock cachedExpenses ?: emptyList()

        val account = accountRepo.getAccount() ?: throw Exception("Error loading account data")
        val today = LocalDate.now().format(dateFormatter)
        val transactions = api.getTransactions(
            accountId = account.id,
            startDate = today,
            endDate = today
        )

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
    }.flowOn(Dispatchers.IO)

    fun clearCache() {
        cachedExpenses = null
    }
}
