package com.example.finances.features.categories.data.mappers

import com.example.finances.features.categories.data.models.CategoryEntity
import com.example.finances.features.categories.data.models.CategoryResponse
import com.example.finances.features.categories.domain.models.Category

fun CategoryResponse.toCategory() = Category(
    id = id,
    name = name,
    emoji = emoji,
    isIncome = isIncome
)

fun CategoryEntity.toCategory() = Category(
    id = id,
    name = name,
    emoji = emoji,
    isIncome = isIncome
)
