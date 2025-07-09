package com.example.finances.features.categories.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория статей
 */
@ActivityScope
class CategoriesRepoImpl @Inject constructor(
    private val categoriesApi: CategoriesApi
) : CategoriesRepo {
    override suspend fun getCategories() = repoTryCatchBlock {
        categoriesApi.getCategories().map { it.toCategory() }.sortedBy { it.name }
    }
}
