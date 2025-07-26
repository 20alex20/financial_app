package com.example.finances.feature.categories.di

import androidx.lifecycle.ViewModel
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import javax.inject.Inject
import javax.inject.Provider

class CategoriesViewModelMapProvider @Inject constructor(
    private val viewModels: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<BaseViewModel>>
) : ViewModelMapProvider {
    override fun provide(): Map<Class<out ViewModel>,
            @JvmSuppressWildcards Provider<BaseViewModel>> = viewModels
}
