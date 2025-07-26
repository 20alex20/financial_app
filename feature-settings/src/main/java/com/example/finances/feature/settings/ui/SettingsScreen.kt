package com.example.finances.feature.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.settings.R
import com.example.finances.feature.settings.domain.settingsItems

@Composable
fun SettingsScreen(navController: NavController) {
    val vm: SettingsViewModel = viewModel(factory = LocalViewModelFactory.current)
    Column(modifier = Modifier.fillMaxSize()) {
        Header(stringResource(R.string.settings))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ListItem(
                mainText = stringResource(R.string.dark_theme),
                height = ListItemHeight.LOW,
                trail = ListItemTrail.Custom {
                    Switch(
                        checked = vm.themeMode.value.darkTheme,
                        onCheckedChange = { vm.changeThemeMode() },
                        colors = SwitchDefaults.colors(
                            uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    )
                }
            )
            settingsItems().forEach { settingsItem ->
                ListItem(
                    mainText = settingsItem.name,
                    height = ListItemHeight.LOW,
                    onClick = {
                        navController.navigate(settingsItem.goTo) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
