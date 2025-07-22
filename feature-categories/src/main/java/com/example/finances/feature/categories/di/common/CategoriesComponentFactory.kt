package com.example.finances.feature.categories.di.common

import com.example.finances.feature.categories.di.CategoriesComponent
import com.example.finances.feature.categories.navigation.CategoriesFeature

object CategoriesComponentFactory {
    fun create(dependencies: CategoriesDependencies): CategoriesFeature {
        return CategoriesComponent.create(dependencies).categoriesFeature()
    }
}
