package com.example.finances.features.account.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.R
import com.example.finances.core.navigation.NavRoutes
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemColorScheme
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.LoadingCircular

@Composable
fun AccountScreen(
    navController: NavController,
    vm: AccountViewModel = viewModel(factory = AccountViewModel.Factory())
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.my_account),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.edit),
                    onClick = {
                        navController.navigate(NavRoutes.EditAccount.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            )
            ListItem(
                mainText = stringResource(R.string.balance),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                emoji = stringResource(R.string.money_bag),
                rightText = vm.state.value.balance
            )
            ListItem(
                mainText = stringResource(R.string.account_name),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                rightText = vm.state.value.accountName
            )
            ListItem(
                mainText = stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                colorScheme = ListItemColorScheme.PRIMARY,
                dividerEnabled = false,
                rightText = vm.state.value.currency
            )
        }

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()
    }
}
