package com.example.finances.feature.transactions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.core.charts.analysis.PieChart
import com.example.finances.core.ui.components.Calendar
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.transactions.R
import com.example.finances.feature.transactions.navigation.ScreenType

@Composable
fun AnalysisScreen(
    screenType: ScreenType,
    navController: NavController
) {
    val vm: AnalysisViewModel = viewModel(
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
                title = stringResource(R.string.analysis),
                backgroundColor = MaterialTheme.colorScheme.surface,
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.back),
                    onClick = { navController.popBackStack() }
                )
            )
            ListItem(
                mainText = stringResource(R.string.period_start),
                height = ListItemHeight.LOW,
                onClick = { isStartCalendarOpen = true },
                trail = ListItemTrail.Custom {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = vm.dates.value.strStart,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            softWrap = false
                        )
                    }
                }
            )
            ListItem(
                mainText = stringResource(R.string.period_end),
                height = ListItemHeight.LOW,
                onClick = { isEndCalendarOpen = true },
                trail = ListItemTrail.Custom {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = vm.dates.value.strEnd,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            softWrap = false
                        )
                    }
                }
            )
            ListItem(
                mainText = stringResource(R.string.sum),
                height = ListItemHeight.LOW,
                rightText = vm.state.value.total
            )
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.height(200.dp)
            ) {
                PieChart(
                    data = vm.state.value.pieChartData,
                    modifier = Modifier
                        .width(168.dp)
                        .fillMaxHeight()
                        .padding(vertical = 16.dp)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(vm.state.value.analysis) { analysisCategory ->
                        ListItem(
                            mainText = analysisCategory.name,
                            rightText = analysisCategory.percent,
                            additionalRightText = analysisCategory.amount,
                            emoji = analysisCategory.emoji,
                            trail = ListItemTrail.LightArrow
                        )
                    }
                }

                if (vm.state.value.analysis.isEmpty()) Text(
                    text = stringResource(R.string.no_period_transactions),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
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
