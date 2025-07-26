package com.example.finances.core.di

import com.example.finances.core.api.CoreAdapter
import com.example.finances.core.api.CoreDependencies
import com.example.finances.core.di.modules.RetrofitClientModule
import dagger.Component

@CoreScope
@Component(
    dependencies = [CoreDependencies::class],
    modules = [RetrofitClientModule::class]
)
interface CoreComponent {
    fun coreAdapter(): CoreAdapter

    @Component.Factory
    interface Factory {
        fun create(dependencies: CoreDependencies): CoreComponent
    }

    companion object {
        fun create(dependencies: CoreDependencies): CoreComponent {
            return DaggerCoreComponent.factory().create(dependencies)
        }
    }
}
