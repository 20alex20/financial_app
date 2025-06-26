package com.example.financial_app.features.account.domain.repo

import com.example.financial_app.common.models.Response
import com.example.financial_app.features.account.domain.models.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepo {
    fun getAccount(): Flow<Response<Account>>
}
