package com.example.finances.features.categories.data

import android.content.Context
import com.example.finances.core.data.network.NetworkManager
import com.example.finances.core.data.repository.repoTryCatchBlock
import com.example.finances.features.categories.data.mappers.toCategory
import com.example.finances.features.categories.domain.repository.CategoriesRepo

/**
 * Имплементация интерфейса репозитория статей
 */
class CategoriesRepoImpl(context: Context) : CategoriesRepo {
    private val api = NetworkManager.provideApi(context, CategoriesApi::class.java)

    override suspend fun getCategories() = repoTryCatchBlock {
        api.getCategories().map { it.toCategory() }.sortedBy { it.name }
    }
}
