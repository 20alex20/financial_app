package com.example.finances.feature.account.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.core.utils.viewmodel.LocalViewModelFactory
import com.example.finances.core.utils.models.Currency
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.HeaderButton
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.ModalSheet
import com.example.finances.core.ui.components.TextInput
import com.example.finances.feature.account.R
import com.example.finances.feature.account.domain.currencySheetItems
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(navController: NavController) {
    val vm: EditAccountViewModel = viewModel(factory = LocalViewModelFactory.current)

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val isSheetOpen = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            val uploadError = Toast.makeText(
                LocalContext.current,
                R.string.error_sending_data,
                Toast.LENGTH_SHORT
            )
            Header(
                title = stringResource(R.string.my_account),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.cancel),
                    onClick = {
                        focusManager.clearFocus()
                        navController.popBackStack()
                    }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.apply),
                    onClick = {
                        focusManager.clearFocus()
                        val success = vm.saveChanges()
                        coroutineScope.launch {
                            if (success.await())
                                navController.popBackStack()
                            else
                                uploadError.show()
                        }
                    }
                )
            )

            val balanceFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.balance),
                height = ListItemHeight.LOW,
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                emoji = stringResource(R.string.money_bag),
                onClick = { balanceFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.balance,
                        updateText = { vm.updateBalance(it) },
                        textAlign = TextAlign.End,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(balanceFocus)
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )

            val accountNameFocus = remember { FocusRequester() }
            ListItem(
                mainText = stringResource(R.string.account_name),
                height = ListItemHeight.LOW,
                paddingValues = PaddingValues(16.dp, 0.dp, 0.dp, 0.dp),
                onClick = { accountNameFocus.requestFocus() },
                trail = ListItemTrail.Custom {
                    TextInput(
                        text = vm.state.value.accountName,
                        updateText = { vm.updateAccountName(it) },
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .focusRequester(accountNameFocus)
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )

            ListItem(
                mainText = stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                rightText = vm.state.value.currency,
                onClick = {
                    focusManager.clearFocus()
                    isSheetOpen.value = true
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            )
        }

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        if (isSheetOpen.value) ModalSheet(
            sheetState = sheetState,
            sheetItems = currencySheetItems(),
            closeSheet = { obj ->
                if (obj is Currency) vm.updateCurrency(obj)
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion { isSheetOpen.value = false }
            }
        ).also { coroutineScope.launch { sheetState.show() } }
    }
}
