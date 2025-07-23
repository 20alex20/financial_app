package com.example.finances.feature.transactions.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionRequest(
    @Json(name = "accountId") val accountId: Int,
    @Json(name = "categoryId") val categoryId: Int,
    @Json(name = "amount") val amount: String,
    @Json(name = "transactionDate") val transactionDate: String,
    @Json(name = "comment") val comment: String
)
