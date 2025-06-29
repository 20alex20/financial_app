package com.example.finances.features.history.data

import android.content.Context
import com.example.finances.core.DateTimeFormatters
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.common.TransactionsHistoryRepoImpl
import com.example.finances.core.data.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.history.domain.mappers.toHistoryRecord
import com.example.finances.features.history.domain.models.HistoryRecord
import com.example.finances.features.history.domain.repo.HistoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import java.io.IOException
import java.time.LocalDate

/**
 * Имплементация интерфейса репозитория экрана истории
 */
class HistoryRepoImpl(
    context: Context,
    accountRepo: AccountRepo
) : TransactionsHistoryRepoImpl(context, accountRepo), HistoryRepo {
    override fun getHistory(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ) : Flow<Response<List<HistoryRecord>>> = repoTryCatchBlock {
        val account = accountRepo.getAccount().last()
        if (account !is Response.Success)
            throw IOException(ERROR_LOADING_ACCOUNT)

        val transaction = api.getTransactions(
            accountId = account.data.id,
            startDate = startDate.format(DateTimeFormatters.dateTime),
            endDate = endDate.format(DateTimeFormatters.dateTime)
        )
        transaction
            .filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { it.toHistoryRecord() }
    }.flowOn(Dispatchers.IO)
}
