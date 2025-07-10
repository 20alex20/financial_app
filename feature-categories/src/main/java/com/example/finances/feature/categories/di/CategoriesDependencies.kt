package com.example.finances.feature.categories.di

import com.example.finances.core.di.ActivityComponent
import retrofit2.Retrofit

interface CategoriesDependencies : ActivityComponent {
    fun retrofit(): Retrofit
}
