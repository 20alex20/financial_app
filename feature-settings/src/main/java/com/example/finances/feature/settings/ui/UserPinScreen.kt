package com.example.finances.feature.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.ui.components.TextInput
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.feature.settings.R

@Composable
fun UserPinScreen(navController: NavController) {
    val vm: UserPinViewModel = viewModel(factory = LocalViewModelFactory.current)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.settings),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.cancel),
                    onClick = {
                        navController.popBackStack()
                    }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.apply),
                    onClick = {
                        if (vm.savePrimaryColor())
                            navController.popBackStack()
                    }
                )
            )

            ListItem(
                mainText = stringResource(R.string.login_with_pin_code),
                trail = ListItemTrail.Custom {
                    Switch(
                        checked = vm.enabled.value,
                        onCheckedChange = { vm.changeEnabled() },
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
            val userPinFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.pin_code),
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                onClick = { userPinFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.userPin.value,
                        updateText = { vm.setUserPin(it) },
                        textAlign = TextAlign.End,
                        keyboardType = KeyboardType.Number,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(userPinFocus)
                    )
                }
            )
            val checkingUserPinFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.confirm),
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                onClick = { checkingUserPinFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.checkingUserPin.value,
                        updateText = { vm.setCheckingUserPin(it) },
                        textAlign = TextAlign.End,
                        keyboardType = KeyboardType.Number,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(checkingUserPinFocus)
                    )
                }
            )
        }

        if (vm.errorType.value != 0) Text(
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondaryContainer,
            text = stringResource(
                when (vm.errorType.value) {
                    1 -> R.string.short_pin_code_error
                    2 -> R.string.unequal_pin_codes
                    else -> R.string.update_saving_error
                }
            )
        )
    }
}
