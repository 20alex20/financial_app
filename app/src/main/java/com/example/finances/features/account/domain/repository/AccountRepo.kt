package com.example.finances.features.account.domain.repository

import com.example.finances.core.data.Response
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.account.domain.models.ShortAccount

/**
 * Интерфейс репозитория счета
 */
interface AccountRepo {
    suspend fun getAccount(): Response<Account>

    suspend fun updateAccount(account: ShortAccount, accountId: Int?): Response<Account>
}
