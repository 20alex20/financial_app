package com.example.finances.feature.categories.api

import retrofit2.Retrofit

interface CategoriesDependencies {
    fun retrofit(): Retrofit

    fun categoriesDatabase(): CategoriesDatabase
}
