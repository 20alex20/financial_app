package com.example.finances.features.account.data

import com.example.finances.features.account.data.models.AccountResponse
import retrofit2.http.GET

/**
 * Источник данных счетов (API для загрузки счетов)
 */
interface AccountApi {
    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>
}
