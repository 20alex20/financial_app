package com.example.finances.feature.categories.di.modules

import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelKey
import com.example.finances.feature.categories.data.database.CategoriesApi
import com.example.finances.feature.categories.data.CategoriesRepoImpl
import com.example.finances.feature.categories.di.CategoriesScope
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import com.example.finances.feature.categories.ui.CategoriesViewModel
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
    @CategoriesScope
    fun bindsCategoriesRepo(categoriesRepoImpl: CategoriesRepoImpl): CategoriesRepo

    companion object {
        @Provides
        @CategoriesScope
        fun providesCategoriesApi(retrofit: Retrofit): CategoriesApi {
            return retrofit.create(CategoriesApi::class.java)
        }
    }
}
