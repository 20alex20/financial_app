package com.example.finances.features.account.domain.repository

import com.example.finances.core.data.network.models.Response
import com.example.finances.features.account.domain.models.Account
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория экрана счета
 */
interface AccountRepo {
    fun getAccount(): Flow<Response<Account>>
}
