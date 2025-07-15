package com.example.finances.features.transactions.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.R
import com.example.finances.core.ui.components.AddButton
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemColorScheme
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.ui.components.models.HeaderButton
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.features.transactions.domain.ScreenType
import com.example.finances.features.transactions.navigation.TransactionsNavRoutes

@Composable
fun ExpensesIncomeScreen(
    screenType: ScreenType,
    navController: NavController
) {
    val vm: ExpensesIncomeViewModel = viewModel(
        factory = LocalViewModelFactory.current,
        extras = remember {
            MutableCreationExtras().apply { set(ViewModelParams.Screen, screenType) }
        }
    )

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = when (screenType) {
                    ScreenType.Expenses -> stringResource(R.string.expenses_today)
                    ScreenType.Income -> stringResource(R.string.income_today)
                },
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.history),
                    onClick = {
                        navController.navigate(TransactionsNavRoutes.History(screenType)) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            )
            ListItem(
                mainText = stringResource(R.string.total),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.state.value.total,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(vm.state.value.expensesIncome) { transaction ->
                    ListItem(
                        mainText = transaction.categoryName,
                        comment = transaction.comment,
                        rightText = transaction.amount,
                        emoji = transaction.categoryEmoji,
                        trail = ListItemTrail.LightArrow,
                        onClick = {
                            navController.navigate(
                                TransactionsNavRoutes.CreateUpdate(screenType, transaction.id)
                            ) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }

        AddButton(
            onClick = {
                navController.navigate(TransactionsNavRoutes.CreateUpdate(screenType)) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
