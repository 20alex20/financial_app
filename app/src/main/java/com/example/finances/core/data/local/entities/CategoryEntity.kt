package com.example.finances.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.transactions.domain.models.ShortCategory

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
) {
    fun toCategory() = Category(
        id = id,
        name = name,
        emoji = emoji,
        isIncome = isIncome
    )

    fun toShortCategory() = ShortCategory(
        id = id,
        name = name
    )

    companion object {
        fun fromCategory(category: Category) = CategoryEntity(
            id = category.id,
            name = category.name,
            emoji = category.emoji,
            isIncome = category.isIncome
        )
    }
} 