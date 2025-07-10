package com.example.finances.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.navigation.models.NavBarItem

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<NavBarItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route::class.java.simpleName
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selected) item.selectedIcon else item.unselectedIcon
                        ),
                        contentDescription = item.title
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(item.route::class.java.simpleName) {
                        popUpTo(NavBarRoutes.Account::class.java.simpleName) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
} 