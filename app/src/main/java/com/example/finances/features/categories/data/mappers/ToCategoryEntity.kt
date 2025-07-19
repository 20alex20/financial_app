package com.example.finances.features.categories.data.mappers

import com.example.finances.features.categories.data.models.CategoryEntity
import com.example.finances.features.categories.domain.models.Category

fun Category.toCategoryEntity() = CategoryEntity(
    id = id,
    name = name,
    emoji = emoji,
    isIncome = isIncome
)
