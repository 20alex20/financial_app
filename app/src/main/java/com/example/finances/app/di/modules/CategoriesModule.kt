package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityComponent
import com.example.finances.app.di.ActivityScope
import com.example.finances.feature.categories.di.common.CategoriesComponentFactory
import com.example.finances.feature.categories.di.common.CategoriesDependencies
import com.example.finances.feature.categories.navigation.CategoriesFeature
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface CategoriesModule {
    @Binds
    fun bindsCategoriesDependencies(dependencies: ActivityComponent): CategoriesDependencies

    companion object {
        @Provides
        @ActivityScope
        fun providesCategoriesNavigation(
            dependencies: CategoriesDependencies
        ): CategoriesFeature {
            return CategoriesComponentFactory.create(dependencies)
        }
    }
}
