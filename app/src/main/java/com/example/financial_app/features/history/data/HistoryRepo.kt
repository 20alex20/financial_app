package com.example.financial_app.features.history.data

import android.content.Context
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.history.domain.models.HistoryRecord
import com.example.financial_app.features.network.data.AccountRepository
import com.example.financial_app.features.network.data.models.Currency
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class HistoryRepo(context: Context, private val isIncome: Boolean) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val accountRepository = AccountRepository(context)
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dateTimeFormatter = DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .appendPattern("'Z'")
        .toFormatter()

    private var cachedHistory: List<HistoryRecord>? = null

    suspend fun getCurrency(): Response<Currency> = withContext(Dispatchers.IO) {
        try {
            Response.Success(Currency.parseStr(accountRepository.getAccount().currency))
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getHistory(): Response<List<HistoryRecord>> = withContext(Dispatchers.IO) {
        try {
            if (cachedHistory != null)
                return@withContext Response.Success(cachedHistory ?: emptyList())

            val account = accountRepository.getAccount()
            val today = LocalDate.now()
            val startOfMonth = today.withDayOfMonth(1)
            val transactions = api.getTransactions(
                accountId = account.id,
                startDate = startOfMonth.format(dateFormatter),
                endDate = today.format(dateFormatter)
            )

            Response.Success(
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
            )
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    fun clearCache() {
        cachedHistory = null
    }
}
