package com.example.financial_app.features.network.data

import android.content.Context
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import com.example.financial_app.features.network.domain.models.AccountResponse
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AccountRepository(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val mutex = Mutex()

    private var cachedAccount: AccountResponse? = null

    suspend fun getAccount(): AccountResponse = mutex.withLock {
        cachedAccount ?: api.getAccounts().firstOrNull()?.also { account ->
            cachedAccount = account
        } ?: throw NoSuchElementException("No accounts found")
    }

    fun clearCache() {
        cachedAccount = null
    }
}
