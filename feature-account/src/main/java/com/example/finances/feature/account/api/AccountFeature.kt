package com.example.finances.feature.account.api

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.Feature
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider
import com.example.finances.feature.account.di.AccountViewModelMapProvider
import com.example.finances.feature.account.domain.repository.ExternalAccountRepo
import com.example.finances.feature.account.navigation.AccountNavigation
import javax.inject.Inject

class AccountFeature @Inject constructor(
    private val accountViewModelMapProvider: AccountViewModelMapProvider,
    private val externalAccountRepo: ExternalAccountRepo
) : Feature {
    override fun getViewModelMapProvider(): ViewModelMapProvider = accountViewModelMapProvider

    fun getExternalAccountRepo() = externalAccountRepo

    override fun registerGraph(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.apply {
            composable<NavBarRoutes.Account> {
                AccountNavigation()
            }
        }
    }
}
