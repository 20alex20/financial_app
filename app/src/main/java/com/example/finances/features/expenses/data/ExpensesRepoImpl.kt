package com.example.finances.features.expenses.data

import android.content.Context
import com.example.finances.common.code.repoTryCatchBlock
import com.example.finances.common.models.Response
import com.example.finances.common.models.Currency
import com.example.finances.features.expenses.domain.models.Expense
import com.example.finances.features.network.domain.NetworkManager
import com.example.finances.features.account.domain.repo.AccountRepo
import com.example.finances.features.expenses.data.models.TransactionResponse
import com.example.finances.features.expenses.domain.repo.ExpensesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExpensesRepoImpl(
    context: Context,
    private val accountRepo: AccountRepo
) : ExpensesRepo {
    private val api: ExpensesApi = NetworkManager.provideApi(context, ExpensesApi::class.java)

    override fun getCurrency(): Flow<Response<Currency>> = accountRepo.getAccount().filterNot {
        it is Response.Loading
    }.map { response ->
        if (response is Response.Success)
            Response.Success(response.data.currency)
        else
            Response.Failure(IOException(ERROR_LOADING_ACCOUNT))
    }.flowOn(Dispatchers.IO)

    private fun convertToExpenses(transaction: TransactionResponse) = Expense(
        id = transaction.id,
        categoryName = transaction.category.name,
        categoryEmoji = transaction.category.emoji,
        amount = transaction.amount.toDouble(),
        comment = transaction.comment
    )

    override fun getExpenses(): Flow<Response<List<Expense>>> = repoTryCatchBlock {
        val account = accountRepo.getAccount().last()
        if (account !is Response.Success)
            throw IOException(ERROR_LOADING_ACCOUNT)

        val today = LocalDate.now().format(dateFormatter)
        val transactions = api.getTransactions(
            accountId = account.data.id,
            startDate = today,
            endDate = today
        )
        transactions
            .filterNot { it.category.isIncome }
            .sortedByDescending { it.transactionDate }
            .map(::convertToExpenses)
    }.flowOn(Dispatchers.IO)

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        private const val ERROR_LOADING_ACCOUNT = "Error loading account data"
    }
}
