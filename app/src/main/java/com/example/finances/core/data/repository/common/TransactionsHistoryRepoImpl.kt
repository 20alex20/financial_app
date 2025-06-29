package com.example.finances.core.data.repository.common

import android.content.Context
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.data.repository.models.Currency
import com.example.finances.features.account.domain.repository.AccountRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException

open class TransactionsHistoryRepoImpl(context: Context, protected val accountRepo: AccountRepo) {
    protected val api = NetworkManager.provideApi(context, TransactionsHistoryApi::class.java)

    fun getCurrency(): Flow<Response<Currency>> {
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

    companion object {
        const val ERROR_LOADING_ACCOUNT = "Error loading account data"
    }
}
