package com.example.financial_app.features.account.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import com.example.financial_app.ui.components.ErrorMessage
import com.example.financial_app.ui.components.ListItem
import com.example.financial_app.ui.components.ListItemColorScheme
import com.example.financial_app.ui.components.ListItemHeight
import com.example.financial_app.ui.components.LoadingCircular
import com.example.financial_app.ui.components.Trail

@Composable
fun CheckScreen(vm: AccountViewModel = viewModel(
    factory = AccountViewModel.Factory(LocalContext.current)
)) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                stringResource(R.string.my_check),
                rightButton = HeaderButton(painterResource(R.drawable.edit), onClick = { })
            )
            ListItem(
                stringResource(R.string.balance),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.balance.value,
                emoji = stringResource(R.string.money_bag),
                trail = Trail.LightArrow(onClick = { })
            )
            ListItem(
                stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.currency.value,
                trail = Trail.LightArrow(onClick = { }),
                dividerEnabled = false
            )
        }

        AddButton(onClick = { })

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
