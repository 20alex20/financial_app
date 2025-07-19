package com.example.finances.features.categories.data.database

import com.example.finances.features.categories.data.models.CategoryResponse
import retrofit2.http.GET

/**
 * Источник данных статей (API для загрузки статей)
 */
interface CategoriesApi {
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>
}
