package com.example.finances.feature.transactions.data.mappers

import com.example.finances.feature.categories.domain.models.Category
import com.example.finances.feature.transactions.domain.models.ShortCategory

fun Category.toShortCategory() = ShortCategory(
    id = id,
    name = name,
    emoji = emoji
)
