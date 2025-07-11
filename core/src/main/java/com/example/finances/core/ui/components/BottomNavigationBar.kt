package com.example.finances.core.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finances.core.navigation.models.NavBarItem

@Composable
fun BottomNavigationBar(
    navController: NavController,
    navBarItems: List<NavBarItem>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        windowInsets = WindowInsets(8.dp, 0.dp, 8.dp, 0.dp),
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        navBarItems.forEach { navBarItem ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(navBarItem.route::class)
                } ?: false,
                onClick = {
                    navController.navigate(navBarItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = navBarItem.image,
                        contentDescription = navBarItem.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    val textMeasurer = rememberTextMeasurer()
                    val textWidth = textMeasurer.measure(
                        text = navBarItem.title,
                        style = MaterialTheme.typography.labelMedium
                    ).size.width

                    var offset by remember { mutableIntStateOf(0) }
                    Text(
                        text = navBarItem.title,
                        style = MaterialTheme.typography.labelMedium,
                        softWrap = false,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .offset { IntOffset(offset, 0) }
                            .onGloballyPositioned { coordinates ->
                                val realWidth = coordinates.size.width
                                if (realWidth < textWidth)
                                    offset = (realWidth - textWidth) / 2
                            }
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
