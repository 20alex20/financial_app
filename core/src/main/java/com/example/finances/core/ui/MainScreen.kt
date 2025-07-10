package com.example.finances.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.finances.core.navigation.AppNavigation
import com.example.finances.core.navigation.navBarItems
import com.example.finances.core.ui.components.BottomNavigationBar

@Composable
fun MainScreen(
    accountNavigation: (NavHostController) -> Unit,
    transactionsNavigation: (NavHostController) -> Unit,
    categoriesNavigation: (NavHostController) -> Unit,
    settingsNavigation: (NavHostController) -> Unit
) {
    val navController = rememberNavController()
    val items = navBarItems()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = items
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(
                navController = navController,
                accountNavigation = accountNavigation,
                transactionsNavigation = transactionsNavigation,
                categoriesNavigation = categoriesNavigation,
                settingsNavigation = settingsNavigation
            )
        }
    }
} 