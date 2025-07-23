package com.example.finances.feature.account.domain.repository

import com.example.finances.core.utils.repository.Response
import com.example.finances.feature.account.domain.models.Account

interface ExternalAccountRepo {
    suspend fun getAccount(): Response<Account>
}
