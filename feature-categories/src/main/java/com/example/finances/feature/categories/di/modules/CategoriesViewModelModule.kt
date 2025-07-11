package com.example.finances.feature.categories.di.modules

import com.example.finances.core.utils.viewmodel.ViewModelProvider
import com.example.finances.feature.categories.ui.CategoriesViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class CategoriesViewModelModule {
    @Provides
    fun categoriesViewModelProvider(
        provider: Provider<CategoriesViewModel>
    ): ViewModelProvider<CategoriesViewModel> {
        return ViewModelProvider { provider.get() }
    }
}
