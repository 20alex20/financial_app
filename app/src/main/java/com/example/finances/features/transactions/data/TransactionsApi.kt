package com.example.finances.features.transactions.data

import com.example.finances.features.transactions.data.models.TransactionResponse
import com.example.finances.features.transactions.data.models.TransactionRequest
import com.example.finances.features.transactions.data.models.ShortTransactionResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Источник данных транзакций (API для загрузки транзакций)
 */
interface TransactionsApi {
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<TransactionResponse>

    @GET("transactions/{transactionId}")
    suspend fun getTransaction(
        @Path("transactionId") transactionId: Int
    ): TransactionResponse

    @POST("transactions")
    suspend fun createTransaction(
        @Body transaction: TransactionRequest
    ): ShortTransactionResponse

    @PUT("transactions/{transactionId}")
    suspend fun updateTransaction(
        @Path("transactionId") transactionId: Int,
        @Body transaction: TransactionRequest
    ): TransactionResponse
}
