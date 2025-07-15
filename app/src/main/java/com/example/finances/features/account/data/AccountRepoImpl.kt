package com.example.finances.features.account.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.account.data.mappers.toAccount
import com.example.finances.features.account.data.mappers.toAccountUpdateRequest
import com.example.finances.features.account.data.extensions.AccountIdLoadingException
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.domain.models.ShortAccount
import com.example.finances.features.account.domain.repository.AccountRepo
import com.example.finances.features.account.domain.repository.ExternalAccountRepo
import com.example.finances.core.data.local.FinanceDatabase
import com.example.finances.core.data.local.entities.AccountEntity
import com.example.finances.core.utils.NetworkConnectionObserver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория счета
 */
@ActivityScope
class AccountRepoImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val database: FinanceDatabase,
    private val networkObserver: NetworkConnectionObserver
) : AccountRepo, ExternalAccountRepo {
    private val mutex = Mutex()
    private var cachedAccount: Account? = null

    override suspend fun getAccount() = repoTryCatchBlock {
        val isOnline = networkObserver.observe().first()

        if (isOnline) {
            // Online mode: fetch from API and update local DB
            cachedAccount ?: mutex.withLock {
                cachedAccount ?: accountApi.getAccounts().first().toAccount().also { account ->
                    cachedAccount = account
                    // Update local DB
                    database.accountDao().insertAccount(AccountEntity.fromAccount(account))
                }
            }
        } else {
            // Offline mode: fetch from local DB
            database.accountDao().getAccount()?.toAccount()
                ?: throw Exception("Account not found in local database")
        }
    }

    override suspend fun updateAccount(account: ShortAccount, accountId: Int?) = repoTryCatchBlock {
        val isOnline = networkObserver.observe().first()
        val id = accountId ?: getAccount().let { accountForId ->
            if (accountForId !is Response.Success)
                throw AccountIdLoadingException()
            accountForId.data.id
        }

        if (isOnline) {
            // Online mode: update API and local DB
            val updatedAccount = accountApi.updateAccount(id, account.toAccountUpdateRequest()).toAccount()
            cachedAccount = updatedAccount
            database.accountDao().updateAccount(AccountEntity.fromAccount(updatedAccount))
            updatedAccount
        } else {
            // Offline mode: update local DB only
            val accountEntity = AccountEntity.fromShortAccount(account, id)
            database.accountDao().updateAccount(accountEntity)
            accountEntity.toAccount()
        }
    }
}
