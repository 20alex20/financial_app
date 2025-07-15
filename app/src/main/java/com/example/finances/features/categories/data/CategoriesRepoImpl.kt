package com.example.finances.features.categories.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.core.data.local.FinanceDatabase
import com.example.finances.core.data.local.entities.CategoryEntity
import com.example.finances.core.utils.NetworkConnectionObserver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Имплементация интерфейса репозитория статей
 */
@ActivityScope
class CategoriesRepoImpl @Inject constructor(
    private val categoriesApi: CategoriesApi,
    private val database: FinanceDatabase,
    private val networkObserver: NetworkConnectionObserver
) : CategoriesRepo {
    private val mutex = Mutex()
    private var cachedCategories: List<Category>? = null

    override suspend fun getCategories() = repoTryCatchBlock {
        val isOnline = networkObserver.observe().first()

        if (isOnline) {
            // Online mode: fetch from API and update local DB
            cachedCategories ?: mutex.withLock {
                cachedCategories ?: categoriesApi.getCategories().map {
                    it.toCategory()
                }.sortedBy { it.name }.also { categories ->
                    cachedCategories = categories
                    // Update local DB
                    database.categoryDao().insertCategories(
                        categories.map { CategoryEntity.fromCategory(it) }
                    )
                }
            }
        } else {
            // Offline mode: fetch from local DB
            database.categoryDao().getAllCategories().map { it.toCategory() }
                .sortedBy { it.name }
        }
    }
}
