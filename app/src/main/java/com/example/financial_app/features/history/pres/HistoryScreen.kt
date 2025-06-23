package com.example.financial_app.features.history.pres

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financial_app.R
import com.example.financial_app.ui.components.Header
import com.example.financial_app.ui.components.HeaderButton
import com.example.financial_app.ui.components.ListItem
import com.example.financial_app.ui.components.ListItemColorScheme
import com.example.financial_app.ui.components.ListItemHeight
import com.example.financial_app.ui.components.Trail

@Composable
fun HistoryScreen(
    parentRoute: String,
    navController: NavController,
    vm: HistoryViewModel = viewModel(
        factory = HistoryViewModel.Factory(LocalContext.current, parentRoute)
    )
) {
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
            rightText = vm.startDateTime.value
        )
        ListItem(
            stringResource(R.string.end),
            height = ListItemHeight.LOW,
            colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
            rightText = vm.endDateTime.value
        )
        ListItem(
            stringResource(R.string.sum),
            height = ListItemHeight.LOW,
            colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
            rightText = vm.total.value,
            dividerEnabled = false
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
                    trail = Trail.LightArrow(onClick = { })
                )
            }
        }
    }
}
