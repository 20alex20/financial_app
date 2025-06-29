package com.example.finances.features.transactions.data

import android.content.Context
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.core.data.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate

/**
 * Имплементация интерфейса репозитория экрана расходов
 */
class TransactionsRepoImpl(
    context: Context,
    private val accountRepo: AccountRepo
) : TransactionsRepo {
    private val api = NetworkManager.provideApi(context, TransactionsApi::class.java)

    override fun getCurrency(): Flow<Response<Currency>> {
        return accountRepo.getAccount()
            .filterNot { it is Response.Loading }
            .map { response ->
                if (response is Response.Success)
                    Response.Success(response.data.currency)
                else
                    Response.Failure(IOException(ERROR_LOADING_ACCOUNT))
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ) = repoTryCatchBlock {
        val account = accountRepo.getAccount().last()
        if (account !is Response.Success)
            throw IOException(ERROR_LOADING_ACCOUNT)

        val transaction = api.getTransactions(
            accountId = account.data.id,
            startDate = startDate.format(DateTimeFormatters.date),
            endDate = endDate.format(DateTimeFormatters.date)
        )
        transaction
            .filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { it.toTransaction() }
    }.flowOn(Dispatchers.IO)

    companion object {
        const val ERROR_LOADING_ACCOUNT = "Error loading account data"
    }
}
