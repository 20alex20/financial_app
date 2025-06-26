package com.example.financial_app.features.account.data

import android.content.Context
import com.example.financial_app.common.code.repoTryCatchBlock
import com.example.financial_app.common.models.Currency
import com.example.financial_app.common.models.Response
import com.example.financial_app.features.account.domain.models.Account
import com.example.financial_app.features.account.domain.repo.AccountRepo
import com.example.financial_app.features.network.domain.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AccountRepoImpl(context: Context) : AccountRepo {
    private val api: AccountApi = NetworkManager.provideApi(context, AccountApi::class.java)
    private val mutex: Mutex = Mutex()

    @Volatile
    private var cachedAccount: Account? = null

    override fun getAccount(): Flow<Response<Account>> = repoTryCatchBlock {
        cachedAccount ?: mutex.withLock {
            cachedAccount ?: api.getAccounts().first().let { account ->
                Account(
                    id = account.id,
                    balance = account.balance.toDouble(),
                    currency = Currency.parseStr(account.currency)
                )
            }.also { cachedAccount = it }
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var instance: AccountRepoImpl? = null

        fun init(context: Context): AccountRepoImpl = instance ?: synchronized(this) {
            instance ?: AccountRepoImpl(context).also { instance = it }
        }
    }
}
