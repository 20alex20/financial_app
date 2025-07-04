package com.example.finances.features.categories.data

import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.repoTryCatchBlock
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.categories.domain.repository.CategoriesRepo

/**
 * Имплементация интерфейса репозитория статей
 */
class CategoriesRepoImpl : CategoriesRepo {
    private val api = NetworkManager.provideApi(CategoriesApi::class.java)

    override suspend fun getCategories() = repoTryCatchBlock {
        api.getCategories().map { it.toCategory() }.sortedBy { it.name }
    }
}
