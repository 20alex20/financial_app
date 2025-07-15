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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория счета
 */
@ActivityScope
class AccountRepoImpl @Inject constructor(
    private val accountApi: AccountApi
): AccountRepo, ExternalAccountRepo {
    private val mutex = Mutex()
    private var cachedAccount: Account? = null

    override suspend fun getAccount() = repoTryCatchBlock {
        cachedAccount ?: mutex.withLock {
            cachedAccount ?: accountApi.getAccounts().first().toAccount().also { account ->
                cachedAccount = account
            }
        }
    }

    override suspend fun updateAccount(account: ShortAccount, accountId: Int?) = repoTryCatchBlock {
        val id = accountId ?: getAccount().let { accountForId ->
            if (accountForId !is Response.Success)
                throw AccountIdLoadingException()
            accountForId.data.id
        }
        accountApi.updateAccount(id, account.toAccountUpdateRequest()).toAccount().also { account ->
            cachedAccount = account
        }
    }
}
