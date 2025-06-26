package com.example.financial_app.features.account.data

import com.example.financial_app.features.account.data.models.AccountResponse
import retrofit2.http.GET

interface AccountApi {
    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>
}
