package com.example.finances.features.account.domain.repository

import com.example.finances.core.data.Response
import com.example.finances.features.account.domain.models.Account

/**
 * Интерфейс репозитория счета
 */
interface AccountRepo {
    suspend fun getAccount(): Response<Account>

    suspend fun updateAccount(account: Account, isRealAccountId: Boolean): Response<Account>
}
