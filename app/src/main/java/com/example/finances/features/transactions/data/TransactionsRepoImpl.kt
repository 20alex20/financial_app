package com.example.finances.features.transactions.data

import android.util.Log
import com.example.finances.core.di.ActivityScope
import com.example.finances.features.transactions.domain.DateTimeFormatters
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.extensions.AccountLoadingException
import com.example.finances.features.account.domain.repository.ExternalAccountRepo
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.features.transactions.data.mappers.toShortCategory
import com.example.finances.features.transactions.data.mappers.toShortTransaction
import com.example.finances.features.transactions.data.mappers.toTransaction
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.example.finances.features.transactions.domain.extensions.CategoriesLoadingException
import com.example.finances.features.transactions.domain.models.ShortTransaction
import com.example.finances.features.transactions.domain.repository.TransactionsRepo
import java.time.LocalDate
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория транзакций
 */
@ActivityScope
class TransactionsRepoImpl @Inject constructor(
    private val accountRepo: ExternalAccountRepo,
    private val categoriesRepo: CategoriesRepo,
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

    override suspend fun getCategories(isIncome: Boolean) = repoTryCatchBlock {
        categoriesRepo.getCategories().let { response ->
            if (response is Response.Success)
                response.data.filter { it.isIncome == isIncome }.map { it.toShortCategory() }
            else
                throw CategoriesLoadingException(CATEGORIES_LOADING_ERROR)
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

    override suspend fun getTransaction(transactionId: Int) = repoTryCatchBlock {
        transactionsApi.getTransaction(transactionId).toShortTransaction()
    }

    override suspend fun createTransaction(shortTransaction: ShortTransaction) = repoTryCatchBlock {
        val account = accountRepo.getAccount()
        if (account !is Response.Success)
            throw AccountLoadingException(ACCOUNT_LOADING_ERROR)

        val h = TransactionRequest(
            accountId = account.data.id,
            categoryId = shortTransaction.categoryId,
            amount = String.format(null, "%.2f", shortTransaction.amount),
            transactionDate = shortTransaction.dateTime.format(
                DateTimeFormatters.requestDateTime
            ),
            comment = shortTransaction.comment
        )
        Log.d("Myyy", "3 " + h)
        transactionsApi.createTransaction(
            h
        ).toShortTransaction()
    }

    override suspend fun updateTransaction(shortTransaction: ShortTransaction) = repoTryCatchBlock {
        val account = accountRepo.getAccount()
        if (account !is Response.Success)
            throw AccountLoadingException(ACCOUNT_LOADING_ERROR)
        if (shortTransaction.id == null)
            throw NullPointerException(NULL_TRANSACTION_ID)
        transactionsApi.updateTransaction(
            shortTransaction.id,
            TransactionRequest(
                accountId = account.data.id,
                categoryId = shortTransaction.categoryId,
                amount = String.format(null, "%.2f", shortTransaction.amount),
                transactionDate = shortTransaction.dateTime.format(
                    DateTimeFormatters.requestDateTime
                ),
                comment = shortTransaction.comment
            )
        ).toShortTransaction()
    }

    companion object {
        const val ACCOUNT_LOADING_ERROR = "Account data loading error"
        const val CATEGORIES_LOADING_ERROR = "Categories data loading error"
        const val NULL_TRANSACTION_ID = "Transaction id is null"
    }
}
