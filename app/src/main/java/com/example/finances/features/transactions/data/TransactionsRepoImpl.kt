package com.example.finances.features.transactions.data

import android.content.Context
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.network.models.Response
import com.example.finances.features.transactions.data.models.AccountLoadingException
import com.example.finances.core.data.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import java.time.LocalDate

/**
 * Имплементация интерфейса репозитория транзакций
 */
class TransactionsRepoImpl(
    context: Context,
    private val accountRepo: AccountRepo
) : TransactionsRepo {
    private val api = NetworkManager.provideApi(context, TransactionsApi::class.java)

    override suspend fun getCurrency() = repoTryCatchBlock {
        accountRepo.getAccount().let { response ->
            if (response is Response.Success)
                response.data.currency
            else
                throw AccountLoadingException(ACCOUNT_LOADING_ERROR)
        }
    }

    override suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        isIncome: Boolean
    ) = repoTryCatchBlock {
        val account = accountRepo.getAccount()
        if (account !is Response.Success)
            throw AccountLoadingException(ACCOUNT_LOADING_ERROR)

        val transaction = api.getTransactions(
            accountId = account.data.id,
            startDate = startDate.format(DateTimeFormatters.requestDate),
            endDate = endDate.format(DateTimeFormatters.requestDate)
        )
        transaction
            .filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { it.toTransaction() }
    }

    companion object {
        const val ACCOUNT_LOADING_ERROR = "Account data loading error"
    }
}
