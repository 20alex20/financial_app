package com.example.finances.feature.account.data.database

import com.example.finances.feature.account.data.models.AccountResponse
import com.example.finances.feature.account.data.models.AccountUpdateRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Источник данных счетов (API для загрузки счетов)
 */
interface AccountApi {
    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>

    @PUT("accounts/{accountId}")
    suspend fun updateAccount(
        @Path("accountId") accountId: Int,
        @Body account: AccountUpdateRequest
    ): AccountResponse
}
