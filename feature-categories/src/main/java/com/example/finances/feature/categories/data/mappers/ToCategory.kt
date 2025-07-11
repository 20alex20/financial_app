package com.example.finances.feature.categories.data.mappers

import com.example.finances.feature.categories.data.models.CategoryResponse
import com.example.finances.feature.categories.domain.models.Category

fun CategoryResponse.toCategory() = Category(
    id = id,
    name = name,
    emoji = emoji
)
