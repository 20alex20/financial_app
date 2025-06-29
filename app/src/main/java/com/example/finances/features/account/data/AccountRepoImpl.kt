package com.example.finances.features.account.data

import android.content.Context
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.mappers.toAccount
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.domain.repository.AccountRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Имплементация интерфейса репозитория экрана счета
 */
class AccountRepoImpl(context: Context) : AccountRepo {
    private val api: AccountApi = NetworkManager.provideApi(context, AccountApi::class.java)
    private val mutex: Mutex = Mutex()

    @Volatile
    private var cachedAccount: Account? = null

    override fun getAccount() = repoTryCatchBlock {
        cachedAccount ?: mutex.withLock {
            cachedAccount ?: api.getAccounts().first().toAccount().also { cachedAccount = it }
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var instance: AccountRepoImpl? = null

        fun init(context: Context) = instance ?: synchronized(this) {
            instance ?: AccountRepoImpl(context).also { instance = it }
        }
    }
}
