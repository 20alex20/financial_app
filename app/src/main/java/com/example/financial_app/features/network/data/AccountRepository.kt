package com.example.financial_app.features.network.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.network.domain.NetworkAdapter
import com.example.financial_app.features.network.domain.api.FinanceApi
import com.example.financial_app.features.network.domain.models.AccountResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AccountRepository private constructor(context: Context) {
    private val api = NetworkAdapter.provideApi(context, FinanceApi::class.java)
    private val mutex = Mutex()

    private var cachedAccount: AccountResponse? = null

    suspend fun getAccount(): AccountResponse? = mutex.withLock {
        if (cachedAccount != null)
            return@withLock cachedAccount

        val account = repoTryCatchBlock {
            api.getAccounts().firstOrNull()
        }.flowOn(Dispatchers.IO).lastOrNull()
        if (account != null && account is Response.Success)
            account.data
        else
            null
    }

    fun clearCache() {
        cachedAccount = null
    }

    companion object {
        @Volatile
        private var instance: AccountRepository? = null

        fun init(context: Context): AccountRepository {
            return instance ?: synchronized(this) {
                instance ?: AccountRepository(context).also { instance = it }
            }
        }
    }
}
