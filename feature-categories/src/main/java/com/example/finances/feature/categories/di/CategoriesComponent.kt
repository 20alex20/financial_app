package com.example.finances.feature.categories.di

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.di.FeatureComponent
import com.example.finances.feature.categories.domain.repository.CategoriesRepository
import com.example.finances.feature.categories.ui.CategoriesViewModel
import dagger.Component

@ActivityScope
@Component(
    dependencies = [CategoriesDependencies::class],
    modules = [CategoriesModule::class]
)
interface CategoriesComponent : FeatureComponent {
    fun categoriesViewModel(): CategoriesViewModel
    fun categoriesRepository(): CategoriesRepository

    @Component.Factory
    interface Factory : FeatureComponent.Factory<CategoriesComponent> {
        override fun create(dependencies: CategoriesDependencies): CategoriesComponent
    }
} 