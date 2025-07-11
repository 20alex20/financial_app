package com.example.finances.feature.categories.di.modules

import com.example.finances.feature.categories.data.CategoriesApi
import com.example.finances.feature.categories.data.CategoriesRepoImpl
import com.example.finances.feature.categories.di.CategoriesScope
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
interface CategoriesRepoModule {
    @Binds
    @CategoriesScope
    fun providesCategoriesRepoImpl(categoriesRepoImpl: CategoriesRepoImpl): CategoriesRepo

    companion object {
        @Provides
        @CategoriesScope
        fun providesCategoriesApi(retrofit: Retrofit): CategoriesApi {
            return retrofit.create(CategoriesApi::class.java)
        }
    }
}
