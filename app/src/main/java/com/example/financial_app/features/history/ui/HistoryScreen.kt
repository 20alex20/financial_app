package com.example.financial_app.features.history.ui

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
import com.example.financial_app.R
import com.example.financial_app.common.graphics.Calendar
import com.example.financial_app.common.graphics.ErrorMessage
import com.example.financial_app.common.graphics.Header
import com.example.financial_app.common.graphics.HeaderButton
import com.example.financial_app.common.graphics.ListItem
import com.example.financial_app.common.graphics.ListItemColorScheme
import com.example.financial_app.common.graphics.ListItemHeight
import com.example.financial_app.common.graphics.LoadingCircular
import com.example.financial_app.common.graphics.Trail

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
    Calendar(showStartCalendar, vm.startDate.value, setNewDate = { newDate ->
        vm.loadWithNewDates(newDate, vm.endDate.value)
    })
    Calendar(showEndCalendar, vm.endDate.value, setNewDate = { newDate ->
        vm.loadWithNewDates(vm.startDate.value, newDate)
    })

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
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.strStart.value,
                onClick = { showStartCalendar.value = true }
            )
            ListItem(
                stringResource(R.string.end),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.strEnd.value,
                onClick = { showEndCalendar.value = true }
            )
            ListItem(
                stringResource(R.string.sum),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                dividerEnabled = false,
                rightText = vm.total.value
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(vm.history.value) { record ->
                    ListItem(
                        record.categoryName,
                        comment = record.comment,
                        rightText = record.amount,
                        additionalRightText = record.dateTime,
                        emoji = record.categoryEmoji,
                        onClick = { },
                        trail = Trail.LightArrow
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
