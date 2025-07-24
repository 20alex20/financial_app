package com.example.finances.core

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.finances.core.navigation.FeatureNavigation
import com.example.finances.core.utils.viewmodel.ViewModelMapProvider

interface Feature : FeatureNavigation {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController)

    fun getViewModelMapProvider(): ViewModelMapProvider
}
