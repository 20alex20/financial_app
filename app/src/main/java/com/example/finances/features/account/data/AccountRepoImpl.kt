package com.example.finances.features.account.data

import com.example.finances.core.data.exceptions.AccountLoadingException
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.Response
import com.example.finances.core.data.repoTryCatchBlock
import com.example.finances.features.account.data.mappers.toAccount
import com.example.finances.features.account.data.mappers.toAccountUpdateRequest
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.domain.models.ShortAccount
import com.example.finances.features.account.domain.repository.AccountRepo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Имплементация интерфейса репозитория счета
 */
class AccountRepoImpl : AccountRepo {
    private val api = NetworkManager.provideApi(AccountApi::class.java)
    private val mutex = Mutex()

    private var cachedAccount: Account? = null

    override suspend fun getAccount() = repoTryCatchBlock {
        cachedAccount ?: mutex.withLock {
            cachedAccount ?: api.getAccounts().first().toAccount().also { cachedAccount = it }
        }
    }

    override suspend fun updateAccount(account: ShortAccount, accountId: Int?) = repoTryCatchBlock {
        val id = accountId ?: getAccount().let { accountForId ->
            if (accountForId !is Response.Success)
                throw AccountLoadingException(ACCOUNT_LOADING_ERROR)
            accountForId.data.id
        }
        api.updateAccount(id, account.toAccountUpdateRequest()).toAccount().also { account ->
            cachedAccount = account
        }
    }

    companion object {
        private const val ACCOUNT_LOADING_ERROR = "Account id loading error"

        @Volatile
        private var instance: AccountRepoImpl? = null

        fun init() = instance ?: synchronized(this) {
            instance ?: AccountRepoImpl().also { instance = it }
        }
    }
}
