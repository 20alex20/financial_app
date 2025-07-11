package com.example.finances.feature.categories.di.modules

import androidx.lifecycle.ViewModel
import com.example.finances.core.di.common.ViewModelKey
import com.example.finances.feature.categories.ui.CategoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CategoriesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun bindsCategoriesViewModel(categoriesViewModel: CategoriesViewModel): ViewModel
}
