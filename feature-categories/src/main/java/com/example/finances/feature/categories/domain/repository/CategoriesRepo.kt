package com.example.finances.feature.categories.domain.repository

import com.example.finances.core.utils.repository.Response
import com.example.finances.feature.categories.domain.models.Category

interface CategoriesRepo {
    suspend fun getCategories(): Response<List<Category>>
}
