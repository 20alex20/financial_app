package com.example.finances.feature.categories.api

import com.example.finances.core.managers.NetworkConnectionObserver
import retrofit2.Retrofit

interface CategoriesDependencies {
    fun retrofit(): Retrofit

    fun networkConnectionObserver(): NetworkConnectionObserver

    fun categoriesDatabase(): CategoriesDatabase
}
