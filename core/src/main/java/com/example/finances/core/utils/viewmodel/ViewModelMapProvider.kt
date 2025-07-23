package com.example.finances.core.utils.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Provider

interface ViewModelMapProvider {
    fun provide(): Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<BaseViewModel>>
}
