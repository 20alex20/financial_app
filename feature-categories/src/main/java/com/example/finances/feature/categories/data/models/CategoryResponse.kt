package com.example.finances.feature.categories.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "emoji") val emoji: String,
    @Json(name = "isIncome") val isIncome: Boolean
)
