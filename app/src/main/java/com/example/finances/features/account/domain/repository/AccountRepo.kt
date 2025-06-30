package com.example.finances.features.account.domain.repository

import com.example.finances.core.data.network.models.Response
import com.example.finances.features.account.domain.models.Account

/**
 * Интерфейс репозитория экрана счета
 */
interface AccountRepo {
    suspend fun getAccount(): Response<Account>
}
