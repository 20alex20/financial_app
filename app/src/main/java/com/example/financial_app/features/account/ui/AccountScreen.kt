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
import com.example.financial_app.common.graphics.AddButton
import com.example.financial_app.common.graphics.Header
import com.example.financial_app.common.graphics.HeaderButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.financial_app.common.graphics.ErrorMessage
import com.example.financial_app.common.graphics.ListItem
import com.example.financial_app.common.graphics.ListItemColorScheme
import com.example.financial_app.common.graphics.ListItemHeight
import com.example.financial_app.common.graphics.LoadingCircular
import com.example.financial_app.common.graphics.Trail

@Composable
fun AccountScreen(vm: AccountViewModel = viewModel(
    factory = AccountViewModel.Factory(LocalContext.current)
)) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                stringResource(R.string.my_account),
                rightButton = HeaderButton(painterResource(R.drawable.edit), onClick = { })
            )
            ListItem(
                stringResource(R.string.balance),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                rightText = vm.balance.value,
                emoji = stringResource(R.string.money_bag),
                onClick = { },
                trail = Trail.LightArrow
            )
            ListItem(
                stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY_CONTAINER,
                dividerEnabled = false,
                rightText = vm.currency.value,
                onClick = { },
                trail = Trail.LightArrow
            )
        }

        AddButton(onClick = { })

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
