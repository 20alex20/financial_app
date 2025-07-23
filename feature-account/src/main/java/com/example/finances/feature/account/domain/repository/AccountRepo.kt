package com.example.finances.feature.account.domain.repository

import com.example.finances.core.utils.repository.Response
import com.example.finances.feature.account.domain.models.Account
import com.example.finances.feature.account.domain.models.ShortAccount

/**
 * Интерфейс репозитория счета
 */
interface AccountRepo {
    suspend fun getAccount(): Response<Account>

    suspend fun updateAccount(account: ShortAccount, accountId: Int?): Response<Account>
}
