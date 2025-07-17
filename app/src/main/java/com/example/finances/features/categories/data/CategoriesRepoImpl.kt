package com.example.finances.features.categories.data

import com.example.finances.core.di.ActivityScope
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.categories.domain.models.Category
import com.example.finances.features.categories.domain.repository.CategoriesRepo
import com.example.finances.core.managers.FinanceDatabase
import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.features.categories.data.database.CategoriesApi
import com.example.finances.features.categories.data.extensions.NoLocalDatabaseCategoriesException
import com.example.finances.features.categories.data.mappers.toCategoryEntity
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

    override suspend fun getCategories() = repoTryCatchBlock(
        isOnline = networkObserver.observe().first()
    ) { localLoading ->
        cachedCategories ?: if (!localLoading) mutex.withLock {
            cachedCategories ?: categoriesApi.getCategories().map { categoryResponse ->
                categoryResponse.toCategory()
            }.also { categories ->
                cachedCategories = categories
                database.categoryDao().insertCategories(
                    categories.map { it.toCategoryEntity() }
                )
            }.sortedBy { it.name }
        } else database.categoryDao().getAllCategories().map { it.toCategory() }.ifEmpty {
            throw NoLocalDatabaseCategoriesException()
        }
    }
}
