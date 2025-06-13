package com.example.financial_app.features.categories.data

import com.example.financial_app.features.categories.domain.models.Category
import com.example.financial_app.features.categories.domain.repo.CategoriesRepoLoader

object CategoriesRepo {
    private var loaded: Boolean = false
    private val loader: CategoriesRepoLoader = CategoriesRepoLoader()

    private var categories: List<Category> = listOf()

    private fun load() {
        val data = loader.loadCategoriesData()
        categories = data.categories
        loaded = true
    }

    fun getCategories(): List<Category> {
        if (!loaded)
            load()
        return categories
    }
}
