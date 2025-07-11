package com.example.finances.feature.categories.ui.models

import com.example.finances.feature.categories.domain.models.Category

data class CategoriesViewModelState(
    val searchQuery: String,
    val filteredCategories: List<Category>
)
