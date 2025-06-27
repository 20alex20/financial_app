package com.example.finances.features.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finances.R
import com.example.finances.features.settings.data.SettingsRepo
import com.example.finances.common.graphics.Header
import com.example.finances.common.graphics.ListItem
import com.example.finances.common.graphics.ListItemHeight
import com.example.finances.common.graphics.Trail

@Composable
fun SettingsScreen(vm: SettingsViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        Header(stringResource(R.string.settings))

        val settings = SettingsRepo.getSettings()
        vm.createSwitchesChecked(settings.size)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(settings) { index, setting ->
                ListItem(
                    setting.name,
                    height = ListItemHeight.LOW,
                    onClick = if (!setting.withSwitch) setting.onClick else null,
                    trail = when (setting.withSwitch) {
                        false -> Trail.DarkArrow
                        true -> Trail.Custom {
                            val switchColors = SwitchDefaults.colors(
                                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                            Switch(
                                checked = vm.switchesChecked.value[index],
                                onCheckedChange = { vm.changeSwitchChecked(index) },
                                colors = switchColors
                            )
                        }
                    }
                )
            }
        }
    }
}
