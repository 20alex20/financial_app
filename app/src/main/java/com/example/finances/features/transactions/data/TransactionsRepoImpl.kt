package com.example.finances.features.transactions.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.repository.ExternalAccountRepo
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.features.transactions.data.mappers.toShortCategory
import com.example.finances.features.transactions.data.mappers.toShortTransaction
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.example.finances.features.transactions.navigation.ScreenType
import com.example.finances.features.transactions.data.extensions.CategoriesLoadingException
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import com.example.finances.features.transactions.data.extensions.AccountLoadingException
import com.example.finances.core.data.local.FinanceDatabase
import com.example.finances.core.data.local.entities.TransactionEntity
import com.example.finances.core.utils.NetworkConnectionObserver
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime
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
            if (response is Response.Success)
                response.data.filter { category ->
                    category.isIncome == (screenType == ScreenType.Income)
                }.map { it.toShortCategory() }
            else
                throw CategoriesLoadingException()
        }
    }

    override suspend fun getTransactions(
        startDate: LocalDate,
        endDate: LocalDate,
        screenType: ScreenType
    ) = repoTryCatchBlock {
        val account = accountRepo.getAccount()
        if (account !is Response.Success)
            throw AccountLoadingException()

        val isOnline = networkObserver.observe().first()
        val accountId = account.data.id
        val isIncome = screenType == ScreenType.Income
        val startDateStr = startDate.format(DateTimeFormatters.requestDate)
        val endDateStr = endDate.format(DateTimeFormatters.requestDate)

        if (isOnline) {
            // Online mode: fetch from API and update local DB
            val transactions = transactionsApi.getTransactions(
                accountId = accountId,
                startDate = startDateStr,
                endDate = endDateStr
            ).filter { it.category.isIncome == isIncome }
            .sortedByDescending { it.transactionDate }
            .map { it.toTransaction() }

            // Update local DB with synced transactions
            transactions.forEach { transaction ->
                database.transactionDao().insertTransaction(
                    TransactionEntity.fromTransaction(
                        transaction = transaction,
                        accountId = accountId,
                        categoryId = transaction.id,
                        isIncome = isIncome,
                        isSynced = true
                    )
                )
            }

            transactions
        } else {
            // Offline mode: fetch from local DB
            database.transactionDao().getTransactions(
                accountId = accountId,
                startDate = LocalDateTime.parse(startDateStr + "T00:00:00.000Z", DateTimeFormatters.replyDateTime),
                endDate = LocalDateTime.parse(endDateStr + "T23:59:59.999Z", DateTimeFormatters.replyDateTime),
                isIncome = isIncome
            ).map { it.toTransaction() }
        }
    }

    override suspend fun getTransaction(transactionId: Int) = repoTryCatchBlock {
        val isOnline = networkObserver.observe().first()

        if (isOnline) {
            val transaction = transactionsApi.getTransaction(transactionId)
            transaction.toShortTransaction()
        } else {
            database.transactionDao().getTransaction(transactionId)?.toShortTransaction()
                ?: throw Exception("Transaction not found in local database")
        }
    }

    override suspend fun createUpdateTransaction(
        shortTransaction: ShortTransaction
    ) = repoTryCatchBlock {
        val account = accountRepo.getAccount()
        if (account !is Response.Success) throw AccountLoadingException()

        val accountId = account.data.id
        val isOnline = networkObserver.observe().first()

        // Get category details for storing in local DB
        val categoryResponse = categoriesRepo.getCategories()
        if (categoryResponse !is Response.Success) throw CategoriesLoadingException()
        val category = categoryResponse.data.find { it.id == shortTransaction.categoryId }
            ?: throw CategoriesLoadingException()

        if (isOnline) {
            // Online mode: send to API and update local DB
            val transactionRequest = TransactionRequest(
                accountId = accountId,
                categoryId = shortTransaction.categoryId,
                amount = String.format(null, "%.2f", shortTransaction.amount),
                transactionDate = shortTransaction.dateTime.format(DateTimeFormatters.requestDateTime),
                comment = shortTransaction.comment
            )

            val response = if (shortTransaction.id == null) {
                transactionsApi.createTransaction(transactionRequest).toShortTransaction()
            } else {
                transactionsApi.updateTransaction(shortTransaction.id, transactionRequest).toShortTransaction()
            }

            // Update local DB with synced transaction
            database.transactionDao().insertTransaction(
                TransactionEntity.fromShortTransaction(
                    shortTransaction = response,
                    accountId = accountId,
                    categoryName = category.name,
                    categoryEmoji = category.emoji,
                    isIncome = category.isIncome,
                    isSynced = true
                )
            )

            response
        } else {
            // Offline mode: save to local DB only
            val transactionEntity = TransactionEntity.fromShortTransaction(
                shortTransaction = shortTransaction,
                accountId = accountId,
                categoryName = category.name,
                categoryEmoji = category.emoji,
                isIncome = category.isIncome,
                isSynced = false
            )

            database.transactionDao().insertTransaction(transactionEntity)
            shortTransaction
        }
    }
}
