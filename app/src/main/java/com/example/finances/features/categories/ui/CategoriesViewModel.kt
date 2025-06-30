package com.example.finances.features.categories.ui

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.finances.core.data.network.models.Response
import com.example.finances.core.ui.viewmodel.BaseViewModel
import com.example.finances.core.ui.viewmodel.ViewModelFactory
import com.example.finances.features.categories.data.CategoriesRepoImpl
import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.features.categories.ui.models.CategoriesViewModelState
import kotlinx.coroutines.launch

/**
 * Вьюмодель экрана статей
 */
class CategoriesViewModel(private val categoriesRepo: CategoriesRepo) : BaseViewModel() {
    private var _allCategories = emptyList<Category>()

    private val _state = mutableStateOf(CategoriesViewModelState("", emptyList()))
    val state: State<CategoriesViewModelState> = _state

    override fun loadData() = viewModelScope.launch {
        try {
            when (val response = categoriesRepo.getCategories()) {
                is Response.Failure -> setError()
                is Response.Success -> {
                    resetLoadingAndError()
                    _allCategories = response.data
                    updateSearchQuery(_state.value.searchQuery)
                }
            }
        } catch (_: Exception) {
            setError()
        }
    }

    fun updateSearchQuery(query: String) {
        val trimQuery = query.trim()
        _state.value = CategoriesViewModelState(
            searchQuery = query,
            filteredCategories = _allCategories.filter { category ->
                category.name.contains(trimQuery, ignoreCase = true)
            }
        )
    }

    init {
        reloadData()
    }

    /**
     * Фабрика по созданию вьюмодели экрана статей и прокидывания в нее репозитория
     */
    class Factory(context: Context) : ViewModelFactory<CategoriesViewModel>(
        viewModelClass = CategoriesViewModel::class.java,
        viewModelInit = { CategoriesViewModel(CategoriesRepoImpl(context)) }
    )
}
