package com.example.finances.feature.categories.api

import com.example.finances.feature.categories.di.CategoriesComponent

object CategoriesComponentFactory {
    fun create(dependencies: CategoriesDependencies): CategoriesFeature {
        return CategoriesComponent.create(dependencies).categoriesFeature()
    }
}
