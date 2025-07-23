package com.example.finances.feature.account.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountUpdateRequest(
    @Json(name = "name") val name: String,
    @Json(name = "balance") val balance: String,
    @Json(name = "currency") val currency: String
)
