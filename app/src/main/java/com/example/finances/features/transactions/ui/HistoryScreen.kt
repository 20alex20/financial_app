package com.example.finances.features.transactions.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.R
import com.example.finances.core.ui.components.Calendar
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemColorScheme
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.Trail

@Composable
fun HistoryScreen(
    parentRoute: String,
    navController: NavController,
    vm: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(LocalContext.current, parentRoute)
    )
) {
    val showStartCalendar = remember { mutableStateOf(false) }
    val showEndCalendar = remember { mutableStateOf(false) }
    Calendar(
        showCalendar = showStartCalendar,
        initialDate = vm.dates.value.start,
        setNewDate = { newDate ->
            vm.setPeriod(newDate, vm.dates.value.end)
            vm.reloadData()
        }
    )
    Calendar(
        showCalendar = showEndCalendar,
        initialDate = vm.dates.value.end,
        setNewDate = { newDate ->
            vm.setPeriod(vm.dates.value.start, newDate)
            vm.reloadData()
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                stringResource(R.string.my_history),
                leftButton = HeaderButton(
                    painterResource(R.drawable.back),
                    onClick = { navController.popBackStack() }),
                rightButton = HeaderButton(painterResource(R.drawable.analysis), onClick = { })
            )
            ListItem(
                stringResource(R.string.start),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.dates.value.strStart,
                onClick = { showStartCalendar.value = true }
            )
            ListItem(
                stringResource(R.string.end),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.dates.value.strEnd,
                onClick = { showEndCalendar.value = true }
            )
            ListItem(
                stringResource(R.string.sum),
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
                        record.categoryName,
                        comment = record.comment,
                        rightText = record.amount,
                        additionalRightText = record.dateTime,
                        emoji = record.categoryEmoji,
                        trail = Trail.LightArrow,
                        onClick = { }
                    )
                }
            }
        }

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
