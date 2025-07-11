package com.example.finances.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.finances.core.R
import com.example.finances.core.ui.components.models.SheetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalSheet(
    sheetState: SheetState,
    sheetItems: List<SheetItem>,
    closeSheet: (Any?) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { closeSheet(null) },
        sheetState = sheetState,
        shape = RoundedCornerShape(36.dp, 36.dp, 0.dp, 0.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth(),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                width = 32.dp,
                height = 4.dp,
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.outline
            )
        }
    ) {
        sheetItems.forEach { currency ->
            SheetListItem(
                text = currency.name,
                icon = currency.icon,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable { closeSheet(currency.obj) }
            )
        }
        SheetListItem(
            text = stringResource(R.string.cancel),
            icon = painterResource(R.drawable.modal_cancel),
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { closeSheet(null) }
        )
    }
}

@Composable
fun SheetListItem(
    text: String,
    icon: Painter,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp, 0.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
} 