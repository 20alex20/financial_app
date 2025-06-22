package com.example.financial_app.features.network.data

import android.content.Context
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import com.example.financial_app.features.network.domain.models.AccountResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    
    private var cachedAccount: AccountResponse? = null

    suspend fun getAccount(): AccountResponse = withContext(Dispatchers.IO) {
        cachedAccount ?: api.getAccounts().firstOrNull()?.also { account -> 
            cachedAccount = account
        } ?: throw NoSuchElementException("No accounts found")
    }

    suspend fun refreshAccount(): AccountResponse = withContext(Dispatchers.IO) {
        api.getAccounts().firstOrNull()?.also { account ->
            cachedAccount = account
        } ?: throw NoSuchElementException("No accounts found")
    }

    fun clearCache() {
        cachedAccount = null
    }
}
