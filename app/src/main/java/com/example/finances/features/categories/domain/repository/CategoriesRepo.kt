package com.example.finances.features.categories.domain.repository

import com.example.finances.core.data.network.models.Response
import com.example.finances.features.categories.domain.models.Category

/**
 * Интерфейс репозитория статей
 */
interface CategoriesRepo {
    suspend fun getCategories(): Response<List<Category>>
}
