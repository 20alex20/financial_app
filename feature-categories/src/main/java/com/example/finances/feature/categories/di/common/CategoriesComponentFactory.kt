package com.example.finances.feature.categories.di.common

import com.example.finances.feature.categories.di.CategoriesComponent
import com.example.finances.feature.categories.navigation.CategoriesNavigation

object CategoriesComponentFactory {
    fun create(dependencies: CategoriesDependencies): CategoriesNavigation {
        return CategoriesComponent.create(dependencies).categoriesNavigation()
    }
}
