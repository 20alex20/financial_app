package com.example.financial_app.features.income.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.network.data.AccountRepository
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeRepo(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepo = AccountRepository.init(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var cachedIncome: List<Income>? = null

    suspend fun getCurrency(): Response<Currency> = withContext(Dispatchers.IO) {
        val strCurrency = accountRepo.getAccount()?.currency
        if (strCurrency != null)
            Response.Success(Currency.parseStr(strCurrency))
        else
            Response.Failure(Exception("Error loading account data"))
    }

    fun getIncome(): Flow<Response<List<Income>>> = repoTryCatchBlock {
        if (cachedIncome != null)
            return@repoTryCatchBlock cachedIncome ?: emptyList()

        val account = accountRepo.getAccount() ?: throw Exception("Error loading account data")
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
    }.flowOn(Dispatchers.IO)

    fun clearCache() {
        cachedIncome = null
    }
}
