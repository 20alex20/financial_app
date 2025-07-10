package com.example.finances.feature.categories.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.feature.categories.data.CategoriesApi
import com.example.finances.feature.categories.data.CategoriesRepoImpl
import com.example.finances.feature.categories.domain.repository.CategoriesRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object CategoriesModule {
    @Provides
    @ActivityScope
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi =
        retrofit.create(CategoriesApi::class.java)

    @Provides
    @ActivityScope
    fun provideCategoriesRepository(
        categoriesApi: CategoriesApi
    ): CategoriesRepository = CategoriesRepoImpl(categoriesApi)
} 