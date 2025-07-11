package com.example.finances.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

/**
 * Base interface for feature navigation.
 * Each feature module should implement this to define its navigation graph.
 */
interface FeatureNavigation {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    )
}
