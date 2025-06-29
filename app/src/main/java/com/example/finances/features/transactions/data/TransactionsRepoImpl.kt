package com.example.finances.features.transactions.data

import android.content.Context
import com.example.finances.core.DateTimeFormatters
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.common.TransactionsHistoryRepoImpl
import com.example.finances.core.data.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.transactions.domain.mappers.toTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import java.io.IOException
import java.time.LocalDate

/**
 * Имплементация интерфейса репозитория экрана расходов
 */
class TransactionsRepoImpl(
    context: Context,
    accountRepo: AccountRepo
) : TransactionsHistoryRepoImpl(context, accountRepo), TransactionsRepo {
    override fun getTransactions(isIncome: Boolean) = repoTryCatchBlock {
        val account = accountRepo.getAccount().last()
        if (account !is Response.Success)
            throw IOException(ERROR_LOADING_ACCOUNT)

        val today = LocalDate.now().format(DateTimeFormatters.date)
        api.getTransactions(account.data.id, today, today)
            .filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { it.toTransaction() }
    }.flowOn(Dispatchers.IO)
}
