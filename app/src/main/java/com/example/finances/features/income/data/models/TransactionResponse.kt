package com.example.finances.features.income.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * data-класс для хранения полученных от сервера данных транзакций
 */
@JsonClass(generateAdapter = true)
data class TransactionResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "account") val account: AccountInfo,
    @Json(name = "category") val category: CategoryInfo,
    @Json(name = "amount") val amount: String,
    @Json(name = "transactionDate") val transactionDate: String,
    @Json(name = "comment") val comment: String,
    @Json(name = "createdAt") val createdAt: String,
    @Json(name = "updatedAt") val updatedAt: String
)

/**
 * data-класс с информацией о счете транзакции
 */
@JsonClass(generateAdapter = true)
data class AccountInfo(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "balance") val balance: String,
    @Json(name = "currency") val currency: String
)

/**
 * data-класс с информацией о статье транзакции
 */
@JsonClass(generateAdapter = true)
data class CategoryInfo(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "emoji") val emoji: String,
    @Json(name = "isIncome") val isIncome: Boolean
)
