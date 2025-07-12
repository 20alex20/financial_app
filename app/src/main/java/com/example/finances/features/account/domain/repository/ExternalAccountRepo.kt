package com.example.finances.features.account.domain.repository

import com.example.finances.core.utils.repository.Response
import com.example.finances.features.account.domain.models.Account

interface ExternalAccountRepo {
    suspend fun getAccount(): Response<Account>
}
