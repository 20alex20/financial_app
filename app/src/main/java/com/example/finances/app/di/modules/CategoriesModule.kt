package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.app.di.CategoriesNavigation
import com.example.finances.app.managers.FinanceDatabase
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.categories.api.CategoriesComponentFactory
import com.example.finances.feature.categories.api.CategoriesDatabase
import com.example.finances.feature.categories.api.CategoriesDependencies
import com.example.finances.feature.categories.api.CategoriesFeature
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
interface CategoriesModule {
    @Binds
    fun bindsCategoriesDependencies(dependencies: ActivityComponent): CategoriesDependencies

    @Binds
    fun bindsCategoriesDatabase(financeDatabase: FinanceDatabase): CategoriesDatabase

    @Binds
    @CategoriesNavigation
    fun bindsCategoriesNavigation(categoriesFeature: CategoriesFeature): FeatureNavigation

    companion object {
        @Provides
        @ActivityScope
        fun providesCategoriesFeature(dependencies: CategoriesDependencies): CategoriesFeature {
            return CategoriesComponentFactory.create(dependencies)
        }

        @Provides
        @IntoSet
        fun providesCategoriesViewModelProviders(
            categoriesFeature: CategoriesFeature
        ): ViewModelMapProvider {
            return categoriesFeature.getViewModelMapProvider()
        }

        @Provides
        fun providesCategoriesRepo(categoriesFeature: CategoriesFeature): CategoriesRepo {
            return categoriesFeature.getCategoriesRepo()
        }
    }
}
