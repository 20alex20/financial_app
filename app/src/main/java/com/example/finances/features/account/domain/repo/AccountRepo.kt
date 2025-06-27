package com.example.finances.features.account.domain.repo

import com.example.finances.common.models.Response
import com.example.finances.features.account.domain.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepo {
    fun getAccount(): Flow<Response<Account>>
}
