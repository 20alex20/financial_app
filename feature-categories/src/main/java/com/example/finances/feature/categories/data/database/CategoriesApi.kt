package com.example.finances.feature.categories.data.database

import com.example.finances.feature.categories.data.models.CategoryResponse
import retrofit2.http.GET

interface CategoriesApi {
    @GET("categories")
    suspend fun getCategories(): List<CategoryResponse>
}
