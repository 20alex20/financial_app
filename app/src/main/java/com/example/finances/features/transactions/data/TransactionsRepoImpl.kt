package com.example.finances.features.transactions.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.ExternalAccountRepo
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.features.transactions.data.mappers.toShortCategory
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.example.finances.features.transactions.navigation.ScreenType
import com.example.finances.features.transactions.data.extensions.CategoriesLoadingException
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.data.extensions.AccountLoadingException
import com.example.finances.core.managers.FinanceDatabase
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.features.transactions.data.database.TransactionsApi
import com.example.finances.features.transactions.data.extensions.NoLocalDatabaseTransactionException
import com.example.finances.features.transactions.data.mappers.toShortTransaction
import com.example.finances.features.transactions.data.mappers.toTransactionEntity
import com.example.finances.features.transactions.data.models.TransactionResponse
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория транзакций
 */
@ActivityScope
class TransactionsRepoImpl @Inject constructor(
    private val accountRepo: ExternalAccountRepo,
    private val categoriesRepo: CategoriesRepo,
    private val transactionsApi: TransactionsApi,
    private val database: FinanceDatabase,
    private val networkObserver: NetworkConnectionObserver
) : TransactionsRepo {
    override suspend fun getCurrency() = repoTryCatchBlock {
        accountRepo.getAccount().let { response ->
            if (response is Response.Success)
                response.data.currency
            else
                throw AccountLoadingException()
        }
    }

    override suspend fun getCategories(screenType: ScreenType) = repoTryCatchBlock {
        categoriesRepo.getCategories().let { response ->
            if (response is Response.Success) response.data.filter { category ->
                category.isIncome == screenType.isIncome
            }.map { category ->
                category.toShortCategory()
            } else {
                throw CategoriesLoadingException()
            }
        }
    }

    private suspend fun toTransactionWithFilter(
        transactionResponse: TransactionResponse,
        screenType: ScreenType
    ) = transactionResponse.toShortTransaction().let { transaction ->
        database.transactionDao().run {
            insertTransaction(
                transaction.toTransactionEntity(
                    id = getTransactionId(transactionResponse.id) ?: 0L,
                    remoteId = transactionResponse.id,
                    isIncome = transactionResponse.category.isIncome,
                    isSynced = true
                )
            )
        }.takeIf { transactionResponse.category.isIncome == screenType.isIncome }?.let { localId ->
            transaction.toTransaction(localId.toInt())
        }
    }

    override suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        screenType: ScreenType
    ) = repoTryCatchBlock(isOnline = networkObserver.observe().first()) { localLoading ->
        if (!localLoading) {
            val account = accountRepo.getAccount()
            if (account !is Response.Success)
                throw AccountLoadingException()
            transactionsApi.getTransactions(
                accountId = account.data.id,
                startDate = startDate.format(DateTimeFormatters.requestDate),
                endDate = endDate.format(DateTimeFormatters.requestDate)
            ).mapNotNull { toTransactionWithFilter(it, screenType) }
        } else database.transactionDao().getTransactions(
            startDate = startDate.atTime(0, 0, 0),
            endDate = endDate.atTime(23, 59, 59, 999),
            isIncome = screenType.isIncome
        ).map { it.toTransaction() }
    }

    override suspend fun getTransaction(transactionId: Int) = repoTryCatchBlock {
        database.transactionDao().getTransaction(
            transactionId.toLong()
        )?.toTransaction() ?: throw NoLocalDatabaseTransactionException()
    }

    private suspend fun remoteSaving(
        transactionRequest: TransactionRequest,
        transactionId: Int?,
        oldTransaction: ShortTransaction,
        screenType: ScreenType
    ) = database.transactionDao().run {
        var remoteId = 0
        val transaction = transactionsApi.run {
            if (transactionId != null && database.transactionDao().getTransactionRemoteId(
                    transactionId.toLong()
                )?.also { remoteId = it } != null
            ) {
                updateTransaction(remoteId, transactionRequest).toShortTransaction()
            } else createTransaction(transactionRequest).also { response ->
                remoteId = response.id
            }.toShortTransaction(oldTransaction.categoryName, oldTransaction.categoryEmoji)
        }
        val id = transactionId?.toLong() ?: 0L
        insertTransaction(transaction.toTransactionEntity(id, remoteId, screenType.isIncome, true))
        transaction
    }

    private suspend fun localSaving(
        transaction: ShortTransaction,
        transactionId: Int?,
        screenType: ScreenType
    ) = database.transactionDao().run {
        val transactionEntity = transaction.toTransactionEntity(
            id = transactionId?.toLong() ?: 0L,
            isIncome = screenType.isIncome,
            isSynced = false,
            remoteId = transactionId?.let { id ->
                database.transactionDao().getTransactionRemoteId(id.toLong())
            }
        )
        getTransaction(
            id = insertTransaction(transactionEntity)
        )?.toShortTransaction() ?: throw NoLocalDatabaseTransactionException()
    }

    override suspend fun createUpdateTransaction(
        transaction: ShortTransaction,
        transactionId: Int?,
        screenType: ScreenType
    ) = repoTryCatchBlock(isOnline = networkObserver.observe().first()) { localLoading ->
        if (!localLoading) {
            val account = accountRepo.getAccount()
            if (account !is Response.Success)
                throw AccountLoadingException()
            TransactionRequest(
                accountId = account.data.id,
                categoryId = transaction.categoryId,
                amount = String.format(null, "%.2f", transaction.amount),
                transactionDate = transaction.dateTime.format(DateTimeFormatters.requestDateTime),
                comment = transaction.comment
            ).let { remoteSaving(it, transactionId, transaction, screenType) }
        } else {
            localSaving(transaction, transactionId, screenType)
        }
    }
}
