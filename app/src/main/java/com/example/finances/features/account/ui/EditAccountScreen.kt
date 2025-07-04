package com.example.finances.features.account.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finances.R
import com.example.finances.core.domain.models.Currency
import com.example.finances.core.ui.components.ErrorMessage
import com.example.finances.core.ui.components.Header
import com.example.finances.core.ui.components.ListItem
import com.example.finances.core.ui.components.ListItemHeight
import com.example.finances.core.ui.components.ListItemTrail
import com.example.finances.core.ui.components.LoadingCircular
import com.example.finances.core.ui.components.ModalSheet
import com.example.finances.core.ui.components.TextInput
import com.example.finances.core.ui.components.models.HeaderButton
import com.example.finances.features.account.domain.currencySheetItems
import kotlinx.coroutines.launch

private const val TEXT_INPUT_WIDTH = 0.5f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    navController: NavController,
    vm: EditAccountViewModel = viewModel(factory = EditAccountViewModel.Factory())
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isSheetOpen by remember { mutableStateOf(false) }
    val sheetItems = currencySheetItems()
    val coroutineScope = rememberCoroutineScope()
    val uploadError = Toast.makeText(
        LocalContext.current,
        R.string.error_sending_data,
        Toast.LENGTH_SHORT
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(
                title = stringResource(R.string.my_account),
                leftButton = HeaderButton(
                    icon = painterResource(R.drawable.cancel),
                    onClick = { navController.popBackStack() }
                ),
                rightButton = HeaderButton(
                    icon = painterResource(R.drawable.apply),
                    onClick = {
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
                        modifier = Modifier
                            .fillMaxWidth(TEXT_INPUT_WIDTH)
                            .focusRequester(balanceFocus)
                            .focusable()
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
                            .fillMaxWidth(TEXT_INPUT_WIDTH)
                            .focusRequester(accountNameFocus)
                            .focusable()
                    )
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )

            ListItem(
                mainText = stringResource(R.string.currency),
                height = ListItemHeight.LOW,
                rightText = vm.state.value.currency,
                onClick = { isSheetOpen = true },
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )
        }

        if (vm.loading.value)
            LoadingCircular()
        if (vm.error.value)
            ErrorMessage()

        if (isSheetOpen) ModalSheet(
            sheetState = sheetState,
            sheetItems = sheetItems,
            closeSheet = { obj ->
                if (obj is Currency)
                    vm.updateCurrency(obj)
                coroutineScope.launch { sheetState.hide() }
                    .invokeOnCompletion { isSheetOpen = false }
            }
        ).also { coroutineScope.launch { sheetState.show() } }
    }
}
