package com.example.finances.feature.settings.ui

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
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.settings.R

@Composable
fun AboutScreen(navController: NavController) {
    val vm: AboutViewModel = viewModel(factory = LocalViewModelFactory.current)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.settings),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.back),
                    onClick = { navController.popBackStack() }
                )
            )

            ListItem(
                mainText = stringResource(R.string.app_version),
                rightText = vm.appInfo.version
            )
            ListItem(
                mainText = stringResource(R.string.last_update),
                rightText = vm.appInfo.lastUpdate
            )
        }
    }
}
