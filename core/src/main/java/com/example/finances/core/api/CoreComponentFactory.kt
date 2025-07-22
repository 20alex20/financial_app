package com.example.finances.core.api

import com.example.finances.core.di.CoreComponent

object CoreComponentFactory {
    fun create(dependencies: CoreDependencies): CoreAdapter {
        return CoreComponent.create(dependencies).coreAdapter()
    }
}
