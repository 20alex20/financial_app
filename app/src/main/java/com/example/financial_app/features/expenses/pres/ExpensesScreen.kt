package com.example.financial_app.features.expenses.pres

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financial_app.R
import com.example.financial_app.ui.components.AddButton
import com.example.financial_app.ui.components.Header
import com.example.financial_app.ui.components.HeaderButton
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.financial_app.ui.components.Lead
import com.example.financial_app.ui.components.ListItem
import com.example.financial_app.ui.components.Trail

@Composable
fun Expenses(vm: ExpensesViewModel = viewModel()) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                stringResource(R.string.expenses_today),
                rightButton = HeaderButton(painterResource(R.drawable.history)) { }
            )
            ListItem(
                stringResource(R.string.total),
                height = 56.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                rightText = vm.balance.value,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(vm.expenses.value) { expense ->
                    ListItem(
                        expense.categoryName,
                        comment = expense.comment,
                        rightText = expense.amount,
                        lead = Lead(expense.emoji),
                        trail = Trail.LightArrow(onClick = { })
                    )
                }
            }
        }

        AddButton(onClick = { })
    }
}
