package com.example.financial_app.features.income.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Response
import com.example.financial_app.common.models.Currency
import com.example.financial_app.features.income.domain.models.Income
import com.example.financial_app.features.account.domain.repo.AccountRepo
import com.example.financial_app.features.income.data.models.TransactionResponse
import com.example.financial_app.features.income.domain.repo.IncomeRepo
import com.example.financial_app.features.network.domain.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class IncomeRepoImpl(
    context: Context,
    private val accountRepo: AccountRepo
) : IncomeRepo {
    private val api: IncomeApi = NetworkManager.provideApi(context, IncomeApi::class.java)

    override fun getCurrency(): Flow<Response<Currency>> = accountRepo.getAccount().filterNot {
        it is Response.Loading
    }.map { response ->
        if (response is Response.Success)
            Response.Success(response.data.currency)
        else
            Response.Failure(Exception(ERROR_LOADING_ACCOUNT))
    }.flowOn(Dispatchers.IO)

    private fun convertToIncome(transaction: TransactionResponse) = Income(
        id = transaction.id,
        categoryName = transaction.category.name,
        categoryEmoji = transaction.category.emoji,
        amount = transaction.amount.toDouble(),
        comment = transaction.comment
    )

    override fun getIncome(): Flow<Response<List<Income>>> = repoTryCatchBlock {
        val account = accountRepo.getAccount().last()
        if (account !is Response.Success)
            throw Exception(ERROR_LOADING_ACCOUNT)

        val today = LocalDate.now().format(dateFormatter)
        val transactions = api.getTransactions(
            accountId = account.data.id,
            startDate = today,
            endDate = today
        )
        transactions
            .filter { it.category.isIncome }
            .sortedByDescending { it.transactionDate }
            .map(::convertToIncome)
    }.flowOn(Dispatchers.IO)

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        private const val ERROR_LOADING_ACCOUNT = "Error loading account data"
    }
}
