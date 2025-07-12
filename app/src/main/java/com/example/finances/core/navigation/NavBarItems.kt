package com.example.finances.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.finances.R
import com.example.finances.core.navigation.models.NavBarItem

@Composable
fun navBarItems() = listOf(
    NavBarItem(
        title = stringResource(R.string.expenses),
        image = painterResource(R.drawable.expenses),
        route = NavBarRoutes.Expenses
    ),
    NavBarItem(
        title = stringResource(R.string.income),
        image = painterResource(R.drawable.income),
        route = NavBarRoutes.Income
    ),
    NavBarItem(
        title = stringResource(R.string.account),
        image = painterResource(R.drawable.account),
        route = NavBarRoutes.Account
    ),
    NavBarItem(
        title = stringResource(R.string.categories),
        image = painterResource(R.drawable.categories),
        route = NavBarRoutes.Categories
    ),
    NavBarItem(
        title = stringResource(R.string.settings),
        image = painterResource(R.drawable.settings),
        route = NavBarRoutes.Settings
    )
)
