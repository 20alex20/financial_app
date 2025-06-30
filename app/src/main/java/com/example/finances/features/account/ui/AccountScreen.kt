package com.example.finances.features.account.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finances.R
import com.example.finances.core.ui.components.AddButton
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemColorScheme
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.Trail

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
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.state.value.balance,
                emoji = stringResource(R.string.money_bag),
                trail = Trail.LightArrow,
                onClick = { }
            )
            ListItem(
                stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                dividerEnabled = false,
                rightText = vm.state.value.currency,
                trail = Trail.LightArrow,
                onClick = { }
            )
        }

        AddButton(onClick = { })

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
