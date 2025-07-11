package com.example.finances.app.di.modules

import com.example.finances.app.di.ActivityScope
import com.example.finances.app.di.AppComponent
import com.example.finances.feature.categories.di.common.CategoriesComponentFactory
import com.example.finances.feature.categories.di.common.CategoriesDependencies
import com.example.finances.feature.categories.navigation.CategoriesNavigation
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface CategoriesModule {
    @Binds
    fun bindsCategoriesDependencies(dependencies: AppComponent): CategoriesDependencies

    companion object {
        @Provides
        @ActivityScope
        fun providesCategoriesNavigation(
            dependencies: CategoriesDependencies
        ): CategoriesNavigation {
            return CategoriesComponentFactory.create(dependencies)
        }
    }
}
