package com.example.finances.features.categories.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.features.categories.data.database.CategoriesApi
import com.example.finances.features.categories.data.CategoriesRepoImpl
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.features.categories.ui.CategoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
interface CategoriesModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun bindsCategoriesViewModel(categoriesViewModel: CategoriesViewModel): BaseViewModel

    @Binds
    @ActivityScope
    fun bindsCategoriesRepo(categoriesRepoImpl: CategoriesRepoImpl): CategoriesRepo

    companion object {
        @Provides
        @ActivityScope
        fun providesCategoriesApi(retrofit: Retrofit): CategoriesApi {
            return retrofit.create(CategoriesApi::class.java)
        }
    }
}
