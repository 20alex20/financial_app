package com.example.finances.core.ui.components.models

import androidx.compose.ui.graphics.painter.Painter

data class HeaderButton(
    val icon: Painter,
    val onClick: () -> Unit
) 