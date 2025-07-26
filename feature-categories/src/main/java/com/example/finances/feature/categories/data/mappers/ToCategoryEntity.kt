package com.example.finances.feature.categories.data.mappers

import com.example.finances.feature.categories.data.models.CategoryEntity
import com.example.finances.feature.categories.domain.models.Category

fun Category.toCategoryEntity() = CategoryEntity(
    id = id,
    name = name,
    emoji = emoji,
    isIncome = isIncome
)
