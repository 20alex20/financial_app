package com.example.finances.core.navigation.models

import androidx.compose.ui.graphics.painter.Painter
import com.example.finances.core.navigation.NavBarRoutes

data class NavBarItem(
    val title: String,
    val image: Painter,
    val route: NavBarRoutes
)
