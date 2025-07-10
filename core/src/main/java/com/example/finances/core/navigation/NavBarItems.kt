package com.example.finances.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.finances.core.R
import com.example.finances.core.navigation.models.NavBarItem

@Composable
fun navBarItems() = remember {
    listOf(
        NavBarItem(
            route = NavBarRoutes.Account,
            selectedIcon = R.drawable.account_selected,
            unselectedIcon = R.drawable.account,
            title = "Account"
        ),
        NavBarItem(
            route = NavBarRoutes.Transactions,
            selectedIcon = R.drawable.transactions_selected,
            unselectedIcon = R.drawable.transactions,
            title = "Transactions"
        ),
        NavBarItem(
            route = NavBarRoutes.Categories,
            selectedIcon = R.drawable.categories_selected,
            unselectedIcon = R.drawable.categories,
            title = "Categories"
        ),
        NavBarItem(
            route = NavBarRoutes.Settings,
            selectedIcon = R.drawable.settings_selected,
            unselectedIcon = R.drawable.settings,
            title = "Settings"
        )
    )
} 