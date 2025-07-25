package com.example.finances.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.finances.core.R

@Composable
fun UserPinScreen(
    userPin: String,
    userPinScreenOpen: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    var inputUserPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextInput(
                text = inputUserPin,
                placeholderText = stringResource(R.string.pin_code),
                keyboardType = KeyboardType.Number,
                updateText = { newInputUserPin ->
                    if (newInputUserPin.length <= 8 && newInputUserPin.isDigitsOnly()) {
                        inputUserPin = newInputUserPin
                        showError = false
                    }
                }
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    if (inputUserPin == userPin) {
                        userPinScreenOpen.value = false
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            }
            if (showError) Text(
                text = stringResource(R.string.error_data_loading),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
}
