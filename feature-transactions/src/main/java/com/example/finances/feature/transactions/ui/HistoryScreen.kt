package com.example.finances.feature.transactions.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.core.ui.components.Calendar
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemColorScheme
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.transactions.R
import com.example.finances.feature.transactions.navigation.ScreenType
import com.example.finances.feature.transactions.navigation.TransactionsNavRoutes

@Composable
fun HistoryScreen(
    screenType: ScreenType,
    navController: NavController
) {
    val vm: HistoryViewModel = viewModel(
        factory = LocalViewModelFactory.current,
        extras = remember {
            MutableCreationExtras().apply { set(ViewModelParams.Screen, screenType) }
        }
    )

    var isStartCalendarOpen by remember { mutableStateOf(false) }
    var isEndCalendarOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.my_history),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.back),
                    onClick = { navController.popBackStack() }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.analysis),
                    onClick = {
                        navController.navigate(
                            TransactionsNavRoutes.Analysis(screenType.isIncome)
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            )
            ListItem(
                mainText = stringResource(R.string.start),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.dates.value.strStart,
                onClick = { isStartCalendarOpen = true }
            )
            ListItem(
                mainText = stringResource(R.string.end),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.dates.value.strEnd,
                onClick = { isEndCalendarOpen = true }
            )
            ListItem(
                mainText = stringResource(R.string.sum),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                dividerEnabled = false,
                rightText = vm.state.value.total
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(vm.state.value.history) { record ->
                    ListItem(
                        mainText = record.categoryName,
                        comment = record.comment,
                        rightText = record.amount,
                        additionalRightText = record.dateTime,
                        emoji = record.categoryEmoji,
                        trail = ListItemTrail.LightArrow,
                        onClick = {
                            navController.navigate(
                                TransactionsNavRoutes.CreateUpdate(screenType.isIncome, record.id)
                            ) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()

        Calendar(
            isCalendarOpen = isStartCalendarOpen,
            initialDate = vm.dates.value.start,
            closeCalendar = { newDate ->
                isStartCalendarOpen = false
                if (newDate != null) {
                    vm.setPeriod(newDate, vm.dates.value.end)
                    vm.reloadData()
                }
            }
        )
        Calendar(
            isCalendarOpen = isEndCalendarOpen,
            initialDate = vm.dates.value.end,
            closeCalendar = { newDate ->
                isEndCalendarOpen = false
                if (newDate != null) {
                    vm.setPeriod(vm.dates.value.start, newDate)
                    vm.reloadData()
                }
            }
        )
    }
}
