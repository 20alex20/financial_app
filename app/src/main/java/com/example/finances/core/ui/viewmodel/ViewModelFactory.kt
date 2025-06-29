package com.example.finances.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class ViewModelFactory<Q : ViewModel>(
    private val viewModelClass: Class<Q>,
    private val viewModelInit: () -> Q
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass))
            return viewModelInit() as T
        throw IllegalArgumentException(UNKNOWN_VIEWMODEL)
    }

    companion object {
        private const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"
    }
}
