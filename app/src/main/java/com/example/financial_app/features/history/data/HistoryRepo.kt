package com.example.financial_app.features.history.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.history.domain.models.HistoryRecord
import com.example.financial_app.features.network.data.AccountRepository
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class HistoryRepo(context: Context, private val isIncome: Boolean) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepo = AccountRepository.init(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dateTimeFormatter = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .appendPattern("'Z'")
        .toFormatter()

    private var cachedHistory: List<HistoryRecord>? = null

    suspend fun getCurrency(): Response<Currency> = withContext(Dispatchers.IO) {
        val strCurrency = accountRepo.getAccount()?.currency
        if (strCurrency != null)
            Response.Success(Currency.parseStr(strCurrency))
        else
            Response.Failure(Exception("Error loading account data"))
    }

    fun getHistory(): Flow<Response<List<HistoryRecord>>> = repoTryCatchBlock {
        if (cachedHistory != null)
            return@repoTryCatchBlock cachedHistory ?: emptyList()

        val account = accountRepo.getAccount() ?: throw Exception("Error loading account data")
        val today = LocalDate.now()
        val transactions = api.getTransactions(
            accountId = account.id,
            startDate = today.withDayOfMonth(1).format(dateFormatter),
            endDate = today.format(dateFormatter)
        )

        transactions
            .filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { transaction ->
                HistoryRecord(
                    id = transaction.id,
                    categoryName = transaction.category.name,
                    categoryEmoji = transaction.category.emoji,
                    dateTime = LocalDateTime.parse(
                        transaction.transactionDate,
                        dateTimeFormatter
                    ),
                    amount = transaction.amount.toDouble(),
                    comment = transaction.comment
                )
            }
            .also { cachedHistory = it }
    }.flowOn(Dispatchers.IO)

    fun clearCache() {
        cachedHistory = null
    }
}
