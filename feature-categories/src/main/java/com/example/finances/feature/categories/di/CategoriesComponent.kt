package com.example.finances.feature.categories.di

import com.example.finances.feature.categories.di.common.CategoriesDependencies
import com.example.finances.feature.categories.di.modules.CategoriesRepoModule
import com.example.finances.feature.categories.di.modules.CategoriesViewModelModule
import com.example.finances.feature.categories.navigation.CategoriesNavigation
import dagger.Component

@CategoriesScope
@Component(
    dependencies = [CategoriesDependencies::class],
    modules = [
        CategoriesRepoModule::class,
        CategoriesViewModelModule::class
    ]
)
interface CategoriesComponent {
    fun categoriesNavigation(): CategoriesNavigation

    @Component.Factory
    interface Factory {
        fun create(dependencies: CategoriesDependencies): CategoriesComponent
    }

    companion object {
        fun create(dependencies: CategoriesDependencies): CategoriesComponent {
            return DaggerCategoriesComponent.factory().create(dependencies)
        }
    }
}
