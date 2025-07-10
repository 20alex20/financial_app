package com.example.finances.feature.categories.data

import com.example.finances.feature.categories.data.models.CategoryResponse
import retrofit2.http.GET

/**
 * Источник данных статей (API для загрузки статей)
 */
interface CategoriesApi {
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>
} 