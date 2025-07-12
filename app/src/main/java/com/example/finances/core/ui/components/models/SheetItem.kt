package com.example.finances.core.ui.components.models

import androidx.compose.ui.graphics.painter.Painter

data class SheetItem(
    val obj: Any,
    val name: String,
    val icon: Painter?
)
