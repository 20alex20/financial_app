package com.example.finances.feature.categories.data

import com.example.finances.core.managers.NetworkConnectionObserver
import com.example.finances.core.utils.repository.repoTryCatchBlock
import com.example.finances.feature.categories.api.CategoriesDatabase
import com.example.finances.feature.categories.data.database.CategoriesApi
import com.example.finances.feature.categories.data.extensions.NoLocalDatabaseCategoriesException
import com.example.finances.feature.categories.data.mappers.toCategory
import com.example.finances.feature.categories.data.mappers.toCategoryEntity
import com.example.finances.feature.categories.domain.models.Category
import com.example.finances.feature.categories.domain.repository.CategoriesRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class CategoriesRepoImpl @Inject constructor(
    private val categoriesApi: CategoriesApi,
    private val database: CategoriesDatabase,
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
