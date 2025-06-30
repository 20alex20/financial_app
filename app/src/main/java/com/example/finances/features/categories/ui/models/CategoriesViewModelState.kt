package com.example.finances.features.categories.ui.models

import com.example.finances.features.categories.domain.models.Category

data class CategoriesViewModelState(
    val searchQuery: String,
    val filteredCategories: List<Category>
)
