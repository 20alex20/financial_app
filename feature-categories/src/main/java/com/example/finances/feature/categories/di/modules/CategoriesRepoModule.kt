package com.example.finances.feature.categories.di.modules

import com.example.finances.feature.categories.data.CategoriesApi
import com.example.finances.feature.categories.di.CategoriesScope
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
interface CategoriesRepoModule {
    @Provides
    @CategoriesScope
    fun providesCategoriesRepo(api: CategoriesApi): CategoriesRepo

    companion object {
        @Provides
        @CategoriesScope
        fun providesCategoriesApi(retrofit: Retrofit): CategoriesApi {
            return retrofit.create(CategoriesApi::class.java)
        }
    }
}
