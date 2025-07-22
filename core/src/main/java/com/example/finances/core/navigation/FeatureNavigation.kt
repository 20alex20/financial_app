package com.example.finances.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureNavigation {
    fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController)
}
