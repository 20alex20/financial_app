package com.example.finances.features.income.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finances.R
import com.example.finances.common.graphics.AddButton
import com.example.finances.common.graphics.Header
import com.example.finances.common.graphics.HeaderButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.finances.features.navigation.data.NavRoutes
import com.example.finances.common.graphics.ErrorMessage
import com.example.finances.common.graphics.ListItem
import com.example.finances.common.graphics.ListItemColorScheme
import com.example.finances.common.graphics.ListItemHeight
import com.example.finances.common.graphics.LoadingCircular
import com.example.finances.common.graphics.Trail

@Composable
fun IncomeScreen(
    navController: NavController,
    vm: IncomeViewModel = viewModel(factory = IncomeViewModel.Factory(LocalContext.current))
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                stringResource(R.string.income_today),
                rightButton = HeaderButton(
                    painterResource(R.drawable.history),
                    onClick = {
                        navController.navigate(
                            NavRoutes.Income.route + "/" + NavRoutes.History.route
                        ) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            )
            ListItem(
                stringResource(R.string.total),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.total.value,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(vm.income.value) { income ->
                    ListItem(
                        income.categoryName,
                        comment = income.comment,
                        rightText = income.amount,
                        emoji = income.categoryEmoji,
                        onClick = { },
                        trail = Trail.LightArrow
                    )
                }
            }
        }

        AddButton(onClick = { })

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
