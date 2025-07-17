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
import com.example.finances.core.managers.FinanceDatabase
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.features.account.data.database.AccountApi
import com.example.finances.features.account.data.extensions.NoLocalAccountDataException
import com.example.finances.features.account.data.mappers.toAccountEntity
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

    override suspend fun getAccount() = repoTryCatchBlock(
        isOnline = networkObserver.observe().first()
    ) { localLoading ->
        cachedAccount ?: if (!localLoading) mutex.withLock {
            cachedAccount ?: accountApi.getAccounts().first().toAccount().also { account ->
                cachedAccount = account
                database.accountDao().insertAccount(account.toAccountEntity())
            }
        } else {
            database.accountDao().getAccount()?.toAccount() ?: throw NoLocalAccountDataException()
        }
    }

    override suspend fun updateAccount(account: ShortAccount, accountId: Int?) = repoTryCatchBlock(
        isOnline = networkObserver.observe().first()
    ) { localLoading ->
        val id = accountId ?: getAccount().let { accountForId ->
            if (accountForId !is Response.Success)
                throw AccountIdLoadingException()
            accountForId.data.id
        }
        if (localLoading) database.accountDao().run {
            updateAccount(account.toAccountEntity(id))
            getAccount()?.toAccount() ?: throw NoLocalAccountDataException()
        } else accountApi.updateAccount(id, account.toAccountUpdateRequest()).toAccount().also {
            cachedAccount = it
            database.accountDao().updateAccount(it.toAccountEntity())
        }
    }
}
