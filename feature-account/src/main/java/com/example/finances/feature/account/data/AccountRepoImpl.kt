package com.example.finances.feature.account.data

import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.feature.account.data.mappers.toAccount
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.feature.account.api.AccountDatabase
import com.example.finances.feature.account.data.database.AccountApi
import com.example.finances.feature.account.data.extensions.AccountIdLoadingException
import com.example.finances.feature.account.data.extensions.NoLocalAccountDataException
import com.example.finances.feature.account.data.mappers.toAccountEntity
import com.example.finances.feature.account.data.mappers.toAccountUpdateRequest
import com.example.finances.feature.account.di.AccountScope
import com.example.finances.feature.account.domain.models.Account
import com.example.finances.feature.account.domain.models.ShortAccount
import com.example.finances.feature.account.domain.repository.AccountRepo
import com.example.finances.feature.account.domain.repository.ExternalTransactionsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория счета
 */
@AccountScope
class AccountRepoImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val database: AccountDatabase,
    private val externalTransactionsRepo: MutableStateFlow<ExternalTransactionsRepo?>,
    private val networkObserver: NetworkConnectionObserver
) : AccountRepo {
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
            getAccount()?.toAccount().also { newAccount ->
                cachedAccount = newAccount
            } ?: throw NoLocalAccountDataException()
        } else accountApi.updateAccount(id, account.toAccountUpdateRequest()).toAccount().also {
            cachedAccount = it
            database.accountDao().updateAccount(it.toAccountEntity())
        }
    }

    override suspend fun getCurrentMonthDifferences(): List<Double> {
        return externalTransactionsRepo.filterNotNull().first().getCurrentMonthDifferences()
    }
}
