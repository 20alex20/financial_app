package com.example.finances.core.navigation.models

import androidx.annotation.DrawableRes
import com.example.finances.core.navigation.NavBarRoutes

data class NavBarItem(
    val route: NavBarRoutes,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val title: String
) 