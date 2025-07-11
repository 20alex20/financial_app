package com.example.finances.features.transactions.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShortTransactionResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "accountId") val accountId: Int,
    @Json(name = "categoryId") val categoryId: Int,
    @Json(name = "amount") val amount: String,
    @Json(name = "transactionDate") val transactionDate: String,
    @Json(name = "comment") val comment: String?,
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "updatedAt") val updatedAt: String
)
