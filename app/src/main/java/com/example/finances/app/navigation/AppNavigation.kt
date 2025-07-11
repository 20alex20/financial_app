package com.example.finances.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.ui.components.BottomNavigationBar

@Composable
fun AppNavigation(
    appNavigationCoordinator: AppNavigationCoordinator,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Column(modifier = modifier) {
        NavHost(
            navController = navController,
            startDestination = NavBarRoutes.Expenses,
            modifier = Modifier.weight(1f)
        ) {
            appNavigationCoordinator.registerGraph(this, navController)
        }

        BottomNavigationBar(
            navController = navController,
            navBarItems = navBarItems(),
            modifier = Modifier.height(80.dp)
        )
    }
}
