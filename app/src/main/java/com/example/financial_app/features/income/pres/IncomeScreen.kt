package com.example.financial_app.features.income.pres

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
import com.example.financial_app.R
import com.example.financial_app.ui.components.AddButton
import com.example.financial_app.ui.components.Header
import com.example.financial_app.ui.components.HeaderButton
import androidx.compose.ui.Alignment
import com.example.financial_app.ui.components.ListItem
import com.example.financial_app.ui.components.ListItemColorScheme
import com.example.financial_app.ui.components.ListItemHeight
import com.example.financial_app.ui.components.Trail

@Composable
fun Income(vm: IncomeViewModel = viewModel()) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                stringResource(R.string.income_today),
                rightButton = HeaderButton(painterResource(R.drawable.history), onClick = { })
            )
            ListItem(
                stringResource(R.string.total),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.balance.value,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(vm.income.value) { expense ->
                    ListItem(
                        expense.categoryName,
                        comment = expense.comment,
                        rightText = expense.amount,
                        trail = Trail.LightArrow(onClick = { })
                    )
                }
            }
        }

        AddButton(onClick = { })
    }
}
