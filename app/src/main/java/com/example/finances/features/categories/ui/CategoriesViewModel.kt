package com.example.finances.features.categories.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.finances.core.utils.repository.Response
import com.example.finances.core.utils.viewmodel.BaseViewModel
import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.features.categories.ui.models.CategoriesViewModelState
import javax.inject.Inject

/**
 * Вьюмодель экрана статей
 */
class CategoriesViewModel @Inject constructor(
    private val categoriesRepo: CategoriesRepo
) : BaseViewModel() {
    private var _allCategories = emptyList<Category>()

    private val _state = mutableStateOf(CategoriesViewModelState("", emptyList()))
    val state: State<CategoriesViewModelState> = _state

    fun updateSearchQuery(query: String) {
        val trimmedQuery = query.trim()
        _state.value = CategoriesViewModelState(
            searchQuery = query,
            filteredCategories = _allCategories.filter { category ->
                category.name.contains(trimmedQuery, ignoreCase = true)
            }
        )
    }

    override suspend fun loadData() {
        when (val response = categoriesRepo.getCategories()) {
            is Response.Failure -> setError()
            is Response.Success -> {
                resetLoadingAndError()
                _allCategories = response.data
                updateSearchQuery(_state.value.searchQuery)
            }
        }
    }

    init {
        reloadData()
    }
}
