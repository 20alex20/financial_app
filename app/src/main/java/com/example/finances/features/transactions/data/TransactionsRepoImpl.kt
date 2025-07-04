package com.example.finances.features.transactions.data

import com.example.finances.core.domain.DateTimeFormatters
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.Response
import com.example.finances.core.data.exceptions.AccountLoadingException
import com.example.finances.core.data.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import java.time.LocalDate

/**
 * Имплементация интерфейса репозитория транзакций
 */
class TransactionsRepoImpl(private val accountRepo: AccountRepo) : TransactionsRepo {
    private val api = NetworkManager.provideApi(TransactionsApi::class.java)

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

        api.getTransactions(
            accountId = account.data.id,
            startDate = startDate.format(DateTimeFormatters.requestDate),
            endDate = endDate.format(DateTimeFormatters.requestDate)
        ).filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { it.toTransaction() }
    }

    companion object {
        const val ACCOUNT_LOADING_ERROR = "Account data loading error"
    }
}
