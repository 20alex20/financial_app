package com.example.finances.features.categories.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.account.domain.models.Account
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория статей
 */
@ActivityScope
class CategoriesRepoImpl @Inject constructor(
    private val categoriesApi: CategoriesApi
) : CategoriesRepo {
    private val mutex = Mutex()
    private var cachedCategories: List<Category>? = null

    override suspend fun getCategories() = repoTryCatchBlock {
        cachedCategories ?: mutex.withLock {
            cachedCategories ?: categoriesApi.getCategories().map {
                it.toCategory()
            }.sortedBy { it.name }.also { cachedCategories = it }
        }
    }
}
