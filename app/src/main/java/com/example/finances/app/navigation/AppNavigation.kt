package com.example.finances.app.navigation

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.finances.core.managers.VibrateUseCase
import com.example.finances.core.navigation.NavBarRoutes
import com.example.finances.core.ui.components.BottomNavigationBar

@RequiresPermission(Manifest.permission.VIBRATE)
@Composable
fun AppNavigation(
    appNavigationCoordinator: AppNavigationCoordinator,
    vibrateUseCase: VibrateUseCase,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Column(modifier = modifier)  {
        NavHost(
            navController = navController,
            startDestination = NavBarRoutes.Expenses,
            modifier = Modifier.weight(1f)
        ) {
            appNavigationCoordinator.registerGraph(this, navController)
        }

        BottomNavigationBar(
            navController = navController,
            navBarItems = navBarItems(),
            vibrateUseCase = vibrateUseCase,
            modifier = Modifier.height(80.dp)
        )
    }
}
