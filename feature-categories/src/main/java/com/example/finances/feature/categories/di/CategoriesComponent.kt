package com.example.finances.feature.categories.di

import com.example.finances.feature.categories.api.CategoriesDependencies
import com.example.finances.feature.categories.di.modules.CategoriesModule
import com.example.finances.feature.categories.api.CategoriesFeature
import dagger.Component

@CategoriesScope
@Component(
    dependencies = [CategoriesDependencies::class],
    modules = [CategoriesModule::class]
)
interface CategoriesComponent {
    fun categoriesFeature(): CategoriesFeature

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
