package com.example.finances.features.transactions.data.mappers

import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.transactions.domain.models.ShortCategory

fun Category.toShortCategory() = ShortCategory(
    id = id,
    name = name
)
