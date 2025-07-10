package com.example.finances.feature.account.data

import com.example.finances.feature.account.data.models.AccountResponse
import com.example.finances.feature.account.data.models.AccountUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface AccountApi {
    @GET("account")
    suspend fun getAccount(): Response<AccountResponse>

    @PUT("account")
    suspend fun updateAccount(@Body request: AccountUpdateRequest): Response<AccountResponse>
} 