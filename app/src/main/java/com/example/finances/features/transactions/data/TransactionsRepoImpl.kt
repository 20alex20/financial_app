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
    override suspend fun getCurrency() = repoTryCatchBlock(
        isOnline = networkObserver.observe().first()
    ) {
        accountRepo.getAccount().let { response ->
            if (response is Response.Success)
                response.data.currency
            else
                throw AccountLoadingException()
        }
    }

    override suspend fun getCategories(screenType: ScreenType) = repoTryCatchBlock(
        isOnline = networkObserver.observe().first()
    ) {
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
    ) = transactionResponse.toTransaction().also { transaction ->
        database.transactionDao().insertTransaction(
            transaction.toTransactionEntity(transactionResponse.category.isIncome)
        )
    }.takeIf { transactionResponse.category.isIncome == screenType.isIncome }

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

    override suspend fun getTransaction(transactionId: Int) = repoTryCatchBlock(
        isOnline = networkObserver.observe().first()
    ) { localLoading ->
        if (transactionId < 0 || localLoading) {
            database.transactionDao().getTransaction(
                transactionId
            )?.toTransaction() ?: throw NoLocalDatabaseTransactionException()
        } else {
            transactionsApi.getTransaction(transactionId).toTransaction()
        }
    }

    private suspend fun remoteSaving(
        transactionRequest: TransactionRequest,
        transactionId: Int?,
        categoryName: String,
        categoryEmoji: String,
        screenType: ScreenType
    ) = transactionsApi.run {
        val transaction = if (transactionId == null || transactionId < 0) {
            if (transactionId != null)
                database.transactionDao().deleteTransaction(transactionId)
            createTransaction(transactionRequest).toTransaction(categoryName, categoryEmoji)
        } else {
            updateTransaction(transactionId, transactionRequest).toTransaction()
        }
        database.transactionDao().insertTransaction(
            transaction.toTransactionEntity(screenType.isIncome)
        )
        transaction
    }

    private suspend fun localSaving(
        transaction: ShortTransaction,
        transactionId: Int?,
        categoryName: String,
        categoryEmoji: String,
        screenType: ScreenType
    ) = database.transactionDao().run {
        if (transactionId != null && transactionId > 0)
            deleteTransaction(transactionId)
        val transactionEntity = transaction.toTransactionEntity(
            localId = if (transactionId != null && transactionId < 0) -transactionId else 0,
            transactionId = transactionId.takeIf { it != null && it > 0 },
            categoryName = categoryName,
            categoryEmoji = categoryEmoji,
            isIncome = screenType.isIncome
        )
        getTransaction(
            transactionId = -insertTransaction(transactionEntity)
        )?.toTransaction() ?: throw NoLocalDatabaseTransactionException()
    }

    override suspend fun createUpdateTransaction(
        transaction: ShortTransaction,
        transactionId: Int?,
        categoryName: String,
        categoryEmoji: String,
        screenType: ScreenType
    ) = repoTryCatchBlock(isOnline = networkObserver.observe().first()) { localLoading ->
        if (!localLoading) {
            val account = accountRepo.getAccount()
            if (account !is Response.Success)
                throw AccountLoadingException()
            val transactionRequest = TransactionRequest(
                accountId = account.data.id,
                categoryId = transaction.categoryId,
                amount = String.format(null, "%.2f", transaction.amount),
                transactionDate = transaction.dateTime.format(DateTimeFormatters.requestDateTime),
                comment = transaction.comment
            )
            remoteSaving(transactionRequest, transactionId, categoryName, categoryEmoji, screenType)
        } else {
            localSaving(transaction, transactionId, categoryName, categoryEmoji, screenType)
        }
    }
}
