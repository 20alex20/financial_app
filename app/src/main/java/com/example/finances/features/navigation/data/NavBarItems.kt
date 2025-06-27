package com.example.finances.features.navigation.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.finances.R
import com.example.finances.features.navigation.domain.models.BarItem

@Composable
fun navBarItems(): List<BarItem> = listOf(
    BarItem(
        title = stringResource(R.string.expenses),
        image = painterResource(R.drawable.expenses),
        route = NavRoutes.Expenses.route
    ),
    BarItem(
        title = stringResource(R.string.income),
        image = painterResource(R.drawable.income),
        route = NavRoutes.Income.route
    ),
    BarItem(
        title = stringResource(R.string.account),
        image = painterResource(R.drawable.account),
        route = NavRoutes.Account.route
    ),
    BarItem(
        title = stringResource(R.string.categories),
        image = painterResource(R.drawable.categories),
        route = NavRoutes.Categories.route
    ),
    BarItem(
        title = stringResource(R.string.settings),
        image = painterResource(R.drawable.settings),
        route = NavRoutes.Settings.route
    )
)
