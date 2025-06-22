package com.example.financial_app.features.navigation.pres

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.financial_app.features.categories.pres.CategoriesScreen
import com.example.financial_app.features.check.pres.CheckScreen
import com.example.financial_app.features.expenses.pres.ExpensesScreen
import com.example.financial_app.features.history.pres.HistoryScreen
import com.example.financial_app.features.income.pres.IncomeScreen
import com.example.financial_app.features.navigation.data.NavRoutes
import com.example.financial_app.features.navigation.data.navBarItems
import com.example.financial_app.features.settings.pres.SettingsScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Column(modifier = modifier) {
        NavHost(
            navController,
            startDestination = NavRoutes.Expenses.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(NavRoutes.Expenses.route) { 
                ExpensesScreen(navController = navController) 
            }
            composable(NavRoutes.Income.route) {
                IncomeScreen(navController = navController)
            }
            composable(NavRoutes.History.route) {
                HistoryScreen(navController = navController)
            }
            composable(NavRoutes.Check.route) { CheckScreen() }
            composable(NavRoutes.Categories.route) { CategoriesScreen() }
            composable(NavRoutes.Settings.route) { SettingsScreen() }
        }
        BottomNavigationBar(
            navController,
            modifier = Modifier.height(80.dp)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        windowInsets = WindowInsets(8.dp, 0.dp, 8.dp, 0.dp),
        modifier = modifier
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        navBarItems().forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(navItem.route) == true,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = navItem.image,
                        contentDescription = navItem.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    val textMeasurer = rememberTextMeasurer()
                    val textWidth = textMeasurer.measure(
                        text = navItem.title,
                        style = MaterialTheme.typography.labelMedium
                    ).size.width

                    var offset by remember { mutableStateOf(0) }
                    Text(
                        text = navItem.title,
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
