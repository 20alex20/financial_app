package com.example.finances.features.transactions.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.extensions.AccountLoadingException
import com.example.finances.features.account.domain.repository.ExternalAccountRepo
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import java.time.LocalDate
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория транзакций
 */
@ActivityScope
class TransactionsRepoImpl @Inject constructor(
    private val accountRepo: ExternalAccountRepo,
    private val transactionsApi: TransactionsApi
) : TransactionsRepo {
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

        transactionsApi.getTransactions(
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
