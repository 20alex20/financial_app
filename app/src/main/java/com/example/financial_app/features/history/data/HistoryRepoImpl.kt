package com.example.financial_app.features.history.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.history.domain.models.HistoryRecord
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.account.domain.repo.AccountRepo
import com.example.financial_app.features.history.data.models.TransactionResponse
import com.example.financial_app.features.history.domain.repo.HistoryRepo
import com.example.financial_app.features.network.domain.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class HistoryRepoImpl(
    context: Context,
    private val accountRepo: AccountRepo
) : HistoryRepo {
    private val api: HistoryApi = NetworkManager.provideApi(context, HistoryApi::class.java)

    override fun getCurrency(): Flow<Response<Currency>> = accountRepo.getAccount().filterNot {
        it is Response.Loading
    }.map { response ->
        if (response is Response.Success)
            Response.Success(response.data.currency)
        else
            Response.Failure(Exception(ERROR_LOADING_ACCOUNT))
    }.flowOn(Dispatchers.IO)

    private fun convertToHistoryRecord(transaction: TransactionResponse) = HistoryRecord(
        id = transaction.id,
        categoryName = transaction.category.name,
        categoryEmoji = transaction.category.emoji,
        dateTime = LocalDateTime.parse(transaction.transactionDate, dateTimeFormatter),
        amount = transaction.amount.toDouble(),
        comment = transaction.comment
    )

    override fun getHistory(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ) : Flow<Response<List<HistoryRecord>>> = repoTryCatchBlock {
        val account = accountRepo.getAccount().last()
        if (account !is Response.Success)
            throw Exception(ERROR_LOADING_ACCOUNT)

        val transaction = api.getTransactions(
            accountId = account.data.id,
            startDate = startDate.format(dateFormatter),
            endDate = endDate.format(dateFormatter)
        )
        transaction
            .filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map(::convertToHistoryRecord)
    }.flowOn(Dispatchers.IO)

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val dateTimeFormatter = DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .appendPattern("'Z'")
            .toFormatter()

        private const val ERROR_LOADING_ACCOUNT = "Error loading account data"
    }
}
