package com.example.financial_app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.financial_app.R

@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(88.dp, 84.dp)
            .padding(16.dp, 14.dp)
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = CircleShape,
            border = BorderStroke(0.dp, MaterialTheme.colorScheme.primary),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Image(
                painter = painterResource(R.drawable.add_button),
                contentDescription = stringResource(R.string.add_button)
            )
        }
    }
}
